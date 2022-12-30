package com.corp.interceptor;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class TransactionInterceptor {

    private final SessionFactory sessionFactory;

    @RuntimeType
    public Object intercept(@SuperCall Callable<Object> call, @Origin Method method) throws Exception {
        Transaction transaction = null;
        boolean transactionStarted = false; // Мы должны отслеживать где стартанула наша транзакция (findById -> saveCompany -> saveLocales)
        if (method.isAnnotationPresent(Transactional.class)) { // Проверяем, есть ли такая аннотация у метода
            transaction = sessionFactory.getCurrentSession().getTransaction(); // Получаем нашу тразакцию
            // Проверяем, активна ли транзакция или нет, потому что мы можем из одного сервиса вызывать метод другого, например когда мы из метода findById вызвали перегруженный метод findById, либо вообще вызван из другого сервиса или других слоев, поэтому мы не должны открывать еще одну транзакцию
            // А так как у нас Thread, мы привязываемся к текущему потоку выполнения, поэтому в рамках одного потока у нас может быть открыта только одна транзакция
            if (!transaction.isActive()) {
                transaction.begin(); // Тогда открываем транзакцию, в противном случае она у нас уже открыта
                transactionStarted = true; // Выставляем что мы стартанули транзакцию
            }
        }

        Object result;
        try { // Как мы знаем, метод пробрасывать какое-либо исключение, либо этот метод может быть заключительным методом в нашем UserService, следовательно мы должны закоммитить нашу транзакцию, поэтому мы все это должны обернуть в блок try
            result = call.call(); // Вызываем наш метод, который может что-то вернуть
            if (transactionStarted) { // Проверяем что мы стартанули транзакцию в этом методе. В противном случае, если мы ее закоммитим в например, saveLocales, то когда вернемся в методы выше, т.е. findById и saveCompany уже транзакции не будет и при обращении к ней будет Exception. Всегда мы должны обрабатывать нашу транзакцию на первой точке доступа к нашему слою Service
                transaction.commit();
            }
        } catch (Exception exception) {
            if (transactionStarted) { // Проверяем что мы стартанули транзакцию в этом методе. В противном случае, если мы ее закоммитим в например, saveLocales, то когда вернемся в методы выше, т.е. findById и saveCompany уже транзакции не будет и при обращении к ней будет Exception. Всегда мы должны обрабатывать нашу транзакцию на первой точке доступа к нашему слою Service
                transaction.rollback();
            }
            throw exception; // Пробрасываем дальше наш Exception. Тот кто вызвал метод Servise`a пусть и заботится об этих ошибках
        }

        return result; // Возвращаем этот результат
    }
}
