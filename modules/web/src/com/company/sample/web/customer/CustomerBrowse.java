package com.company.sample.web.customer;

import com.company.sample.entity.Customer;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.GroupDatasource;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;

public class CustomerBrowse extends AbstractLookup {

    @Inject
    private GroupDatasource<Customer, UUID> customersDs;

    @Override
    public void init(Map<String, Object> params) {
        // add listener on datasource content change
        customersDs.addCollectionChangeListener(e -> {
            // find page start
            int counter = ((CollectionDatasource.SupportsPaging) customersDs).getFirstResult();
            // for each entity in the datasource, set its sequence number
            for (Customer customer : customersDs.getItems()) {
                customer.setLineNum(++counter);
            }
            // make the datasource unmodified to prevent from asking to save changes on screen close
            ((DatasourceImplementation) customersDs).setModified(false);
        });
    }
}