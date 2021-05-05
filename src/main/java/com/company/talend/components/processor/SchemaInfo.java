package com.company.talend.components.processor;

import java.io.Serializable;

import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.configuration.ui.layout.GridLayout;
import org.talend.sdk.component.api.meta.Documentation;

@GridLayout({ @GridLayout.Row({ "label", "key", "talendType", "nullable", "pattern" }) })
@Documentation("Schema definition.")
public class SchemaInfo implements Serializable {

    @Option
    @Documentation("Column name.")
    private String label;

    @Option
    @Documentation("Is it a Key column.")
    private boolean key;

    @Option
    @Documentation("Talend type such as id_String.")
    private String talendType;

    @Option
    @Documentation("Is it a Nullable column.")
    private boolean nullable;

    @Option
    @Documentation("Pattern used for datetime processing.")
    private String pattern = "yyyy-MM-dd HH:mm";

    public String getLable(){
        return label;

    }

}