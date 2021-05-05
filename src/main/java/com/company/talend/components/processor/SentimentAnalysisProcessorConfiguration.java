package com.company.talend.components.processor;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.configuration.action.Proposable;
import org.talend.sdk.component.api.configuration.ui.layout.GridLayout;
import org.talend.sdk.component.api.meta.Documentation;
import org.talend.sdk.component.api.configuration.ui.widget.Structure;

@Data
@GridLayout({
        // the generated layout put one configuration entry per line,
        // customize it as much as needed
        @GridLayout.Row({"incomingSchema"}),
        @GridLayout.Row({"AWS_AccessKey"}),
        @GridLayout.Row({"AWS_SecretKey"}),
        @GridLayout.Row({"AWS_RegionName"}),
        @GridLayout.Row({"InputTextColumn"})
})
@Documentation("TODO fill the documentation for this configuration")
public class SentimentAnalysisProcessorConfiguration implements Serializable {
    @Option
    @Documentation("TODO fill the documentation for this parameter")
    private String AWS_AccessKey;

    @Option
    @Documentation("TODO fill the documentation for this parameter")
    private String AWS_SecretKey;

    @Option
    @Documentation("TODO fill the documentation for this parameter")
    private String AWS_RegionName;

    @Option
    @Documentation("TODO fill the documentation for this parameter")
    @Proposable("dummy")
    private String InputTextColumn;

    @Option
    @Documentation("Incoming metadata.")
    @Structure(type = Structure.Type.IN)
    private List<SchemaInfo> incomingSchema;


    public SchemaInfo getincomingSchema(int i){

        return incomingSchema.get(i);
    }

    public SentimentAnalysisProcessorConfiguration setincomingSchema(SchemaInfo s){

        incomingSchema.add(s);
        return this;
    }


    public String getAWS_AccessKey() {
        return AWS_AccessKey;
    }

    public SentimentAnalysisProcessorConfiguration setAWS_AccessKey(String AWS_AccessKey) {
        this.AWS_AccessKey = AWS_AccessKey;
        return this;
    }

    public String getAWS_SecretKey() {
        return AWS_SecretKey;
    }

    public SentimentAnalysisProcessorConfiguration setAWS_SecretKey(String AWS_SecretKey) {
        this.AWS_SecretKey = AWS_SecretKey;
        return this;
    }

    public String getAWS_RegionName() {
        return AWS_RegionName;
    }

    public SentimentAnalysisProcessorConfiguration setAWS_RegionName(String AWS_RegionName) {
        this.AWS_RegionName = AWS_RegionName;
        return this;
    }

    public String getInputTextColumn() {
        return InputTextColumn;
    }

    public SentimentAnalysisProcessorConfiguration setInputTextColumn(String InputTextColumn) {
        this.InputTextColumn = InputTextColumn;
        return this;
    }
}