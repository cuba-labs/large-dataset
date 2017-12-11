package com.company.sample.service;

import com.company.sample.entity.Customer;
import com.company.sample.entity.Order;
import com.google.common.base.Strings;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.Metadata;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service(DataGenerationService.NAME)
public class DataGenerationServiceBean implements DataGenerationService {

    public static final int CUST_COUNT = 1000;
    public static final int ORDER_COUNT = 500000;

    @Inject
    private Logger log;

    @Inject
    private Persistence persistence;

    @Inject
    private Metadata metadata;

    @Override
    public void generateData() {
        cleanupDatabase();
        List<Customer> customers = createCustomers();
        createOrders(customers);
    }

    private void createOrders(List<Customer> customers) {
        log.info("Creating " + ORDER_COUNT + " orders");
        try (Transaction tx = persistence.createTransaction()) {
            for (int i = 0; i < ORDER_COUNT; i++) {
                Order order = metadata.create(Order.class);
                order.setCustomer(getRandomCustomer(customers));
                order.setDate(getRandomDate());
                order.setNum(Strings.padStart(String.valueOf(i), 6, '0'));
                order.setAmount(new BigDecimal(Math.random() * 1000));
                persistence.getEntityManager().persist(order);

                if (i % 1000 == 0) {
                    tx.commitRetaining();
                    log.info("Created orders : " + i);
                }
            }
            tx.commit();
        }
    }

    private Date getRandomDate() {
        return DateUtils.addDays(new Date(), (int) ((-1) * Math.random() * 365));
    }

    private Customer getRandomCustomer(List<Customer> customers) {
        return customers.get((int) (Math.random() * CUST_COUNT));
    }

    private List<Customer> createCustomers() {
        log.info("Creating " + CUST_COUNT + " customers");
        List<Customer> customers = new ArrayList<>();
        try (Transaction tx = persistence.createTransaction()) {
            for (int i = 0; i < CUST_COUNT; i++) {
                Customer customer = metadata.create(Customer.class);
                String num = Strings.padStart(String.valueOf(i), 4, '0');
                customer.setName("customer-" + num);
                customer.setAddress("address-" + num);
                persistence.getEntityManager().persist(customer);
                customers.add(customer);
            }
            tx.commit();
        }
        return customers;
    }

    private void cleanupDatabase() {
        try (Transaction tx = persistence.createTransaction()) {
            log.info("Removing orders");
            Query query = persistence.getEntityManager().createNativeQuery("delete from SAMPLE_ORDER");
            query.executeUpdate();

            log.info("Removing customers");
            query = persistence.getEntityManager().createNativeQuery("delete from SAMPLE_CUSTOMER");
            query.executeUpdate();

            tx.commit();
        }
    }
}