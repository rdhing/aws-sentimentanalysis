package com.company.talend.components.service;

import com.amazonaws.regions.Regions;
import com.company.talend.components.processor.SchemaInfo;
import com.company.talend.components.processor.SentimentAnalysisProcessorConfiguration;
import org.talend.sdk.component.api.record.Record;
import org.talend.sdk.component.api.record.Schema;
import org.talend.sdk.component.api.service.Service;
import org.talend.sdk.component.api.service.completion.DynamicValues;
import org.talend.sdk.component.api.service.completion.Values;
import org.talend.sdk.component.api.configuration.Option;

import java.util.ArrayList;
import java.util.List;

@Service
public class AwsSentimentanalysisService {

    // you can put logic here you can reuse in components

    @DynamicValues("dummy")
    public Values columns() {
        Values values = new Values();
        List<Values.Item> items = new ArrayList<Values.Item>();

        for (SchemaInfo s : conn.getIncomingSchema()) {
            items.add(new Values.Item(s.getLable(),s.getLable()));
        }

        values.setItems(items);
        return values;
    }
}