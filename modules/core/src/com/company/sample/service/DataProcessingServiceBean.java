package com.company.sample.service;

import com.company.sample.entity.Order;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TransactionParams;
import com.haulmont.cuba.core.TypedQuery;
import org.eclipse.persistence.queries.CursoredStream;
import org.eclipse.persistence.queries.ReadAllQuery;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service(DataProcessingService.NAME)
public class DataProcessingServiceBean implements DataProcessingService {

    @Inject
    private Logger log;

    @Inject
    private Persistence persistence;

    @Override
    public Map<String, BigDecimal> process1() {
        log.info("Processing method 1");

        List<UUID> idList = loadIdList();

        HashMap<String, BigDecimal> result = new HashMap<>();

        AtomicInteger counter = new AtomicInteger();
        idList.stream()
                .collect(Collectors.groupingBy(o -> counter.getAndIncrement() / 100))
                .forEach((chunk, chunkIds) -> {
                    try (Transaction tx = persistence.createTransaction(new TransactionParams().setReadOnly(true))) {
                        TypedQuery<Order> query = persistence.getEntityManager().createQuery(
                                "select o from sample$Order o where o.id in ?1", Order.class);
                        query.setParameter(1, chunkIds);
                        List<Order> orders = query.getResultList();
                        for (Order order : orders) {
                            processOrder(order.getDate(), order.getAmount(), result);
                        }
                        tx.commit();
                    }
                    log.info("Processed orders: " + chunk * 100);
                });

        return result;
    }

    @Override
    public Map<String, BigDecimal> process2() {
        log.info("Processing method 2");

        HashMap<String, BigDecimal> result = new HashMap<>();

        try (Transaction tx = persistence.createTransaction(new TransactionParams().setReadOnly(true))) {
            UnitOfWork unitOfWork = persistence.getEntityManager().getDelegate().unwrap(UnitOfWork.class);

            ReadAllQuery query = new ReadAllQuery(Order.class);
            query.useCursoredStream();
            CursoredStream stream = (CursoredStream) unitOfWork.executeQuery(query);

            int i = 0;
            while (!stream.atEnd()) {
                Order order = (Order) stream.read();
                processOrder(order.getDate(), order.getAmount(), result);
                stream.releasePrevious();

                i++;
                if (i % 100 == 0) {
                    log.info("Processed orders: " + i);
                }
            }
            stream.close();

            tx.commit();
        }

        return result;
    }

    private List<UUID> loadIdList() {
        List<UUID> idList;
        try (Transaction tx = persistence.createTransaction(new TransactionParams().setReadOnly(true))) {
            TypedQuery<UUID> query = persistence.getEntityManager().createQuery(
                    "select o.id from sample$Order o", UUID.class);
            idList = query.getResultList();
            tx.commit();
        }
        return idList;
    }

    private void processOrder(Date orderDate, BigDecimal orderAmount, HashMap<String, BigDecimal> result) {
        String key = new SimpleDateFormat("yyyy-MM").format(orderDate);
        BigDecimal value = result.computeIfAbsent(key, s -> BigDecimal.ZERO);
        result.put(key, value.add(orderAmount));
    }
}