package com.company.talend.components.processor;

import static org.talend.sdk.component.api.component.Icon.IconType.CUSTOM;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.talend.sdk.component.api.component.Icon;
import org.talend.sdk.component.api.component.Version;
import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.meta.Documentation;
import org.talend.sdk.component.api.processor.AfterGroup;
import org.talend.sdk.component.api.processor.BeforeGroup;
import org.talend.sdk.component.api.processor.ElementListener;
import org.talend.sdk.component.api.processor.Input;
import org.talend.sdk.component.api.processor.Output;
import org.talend.sdk.component.api.processor.OutputEmitter;
import org.talend.sdk.component.api.processor.Processor;
import org.talend.sdk.component.api.record.Record;

import com.company.talend.components.service.AwsSentimentanalysisService;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.comprehend.AmazonComprehend;
import com.amazonaws.services.comprehend.AmazonComprehendClientBuilder;
import com.amazonaws.services.comprehend.model.DetectSentimentRequest;
import com.amazonaws.services.comprehend.model.DetectSentimentResult;
import org.talend.sdk.component.api.record.Schema;
import org.talend.sdk.component.api.service.record.RecordBuilderFactory;
import com.jayway.jsonpath.*;

@Version(1) // default version is 1, if some configuration changes happen between 2 versions you can add a migrationHandler
@Icon(value = CUSTOM, custom = "SentimentAnalysis") // icon is located at src/main/resources/icons/SentimentAnalysis.svg
@Processor(name = "SentimentAnalysis")
@Documentation("TODO fill the documentation for this processor")
public class SentimentAnalysisProcessor implements Serializable {
    private final SentimentAnalysisProcessorConfiguration configuration;
    private final AwsSentimentanalysisService service;
    private final RecordBuilderFactory builderFactory;
    private AmazonComprehend comprehendClient;


    public SentimentAnalysisProcessor(@Option("configuration") final SentimentAnalysisProcessorConfiguration configuration,
                          final AwsSentimentanalysisService service,
                          final RecordBuilderFactory builderFactory) {
        this.configuration = configuration;
        this.service = service;
        this.builderFactory = builderFactory;
    }

    @PostConstruct
    public void init() {
        // this method will be executed once for the whole component execution,
        // this is where you can establish a connection for instance
        // Note: if you don't need it you can delete it
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(configuration.getAWS_AccessKey(),configuration.getAWS_SecretKey());
        comprehendClient = AmazonComprehendClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion(configuration.getAWS_RegionName()).build();

    }

    @BeforeGroup
    public void beforeGroup() {
        // if the environment supports chunking this method is called at the beginning if a chunk
        // it can be used to start a local transaction specific to the backend you use
        // Note: if you don't need it you can delete it
    }

    @ElementListener
    public void onNext(
            @Input final Record defaultInput,
            @Output final OutputEmitter<Record> defaultOutput) {
        // this is the method allowing you to handle the input(s) and emit the output(s)
        // after some custom logic you put here, to send a value to next element you can use an
        // output parameter and call emit(value).

        Map<String, String> items = new HashMap<>();
        System.out.println("defaultinput " + defaultInput);
        String[] temp = defaultInput.toString().split(",\"");
        System.out.println("temp" + temp);
        for(String s : temp){
            System.out.println("temp" + s);

            s = s.replaceAll("[{}\"]","");
            String[] data = s.split(":");
            System.out.println("s-->" + s);
            System.out.println("data[0]:" + data[0] + "--" + "data[1]:" + data[1]);
            items.put(data[0],data[1]);
        }

        // Call Sentiment Detection API
        DetectSentimentRequest detectSentimentRequest = new DetectSentimentRequest().withText(items.get(configuration.getInputTextColumn())).withLanguageCode("en");
        String response_JSON=comprehendClient.detectSentiment(detectSentimentRequest).toString();

        //System.out.println("Response-->"+ response_JSON);

        Record.Builder builder = builderFactory.newRecordBuilder();
        DocumentContext  jsonContext= null;
        jsonContext = JsonPath.parse(response_JSON);

        /*System.out.println("Sentiment" + jsonContext.read("$.Sentiment"));
        System.out.println("Sentiment" + jsonContext.read("$.SentimentScore.Positive").toString());
        System.out.println("Sentiment" + jsonContext.read("$.SentimentScore.Negative").toString());
        System.out.println("Sentiment" + jsonContext.read("$.SentimentScore.Neutral").toString());
        System.out.println("Sentiment" + jsonContext.read("$.SentimentScore.Mixed").toString());
        */

        for (Map.Entry<String, String> pair: items.entrySet()) {
            //builder.withString(configuration.getInputTextColumn(),items.get(configuration.getInputTextColumn()));
            builder.withString(pair.getKey(), pair.getValue());
        }
            builder.withString("Sentiment",jsonContext.read("$.Sentiment"));
            builder.withString("Positive",jsonContext.read("$.SentimentScore.Positive").toString());
            builder.withString("Negative",jsonContext.read("$.SentimentScore.Negative").toString());
            builder.withString("Neutral",jsonContext.read("$.SentimentScore.Neutral").toString());
            builder.withString("Mixed",jsonContext.read("$.SentimentScore.Mixed").toString());


        Record record = builder.build();
        defaultOutput.emit(record);

    }

    @AfterGroup
    public void afterGroup() {
        // symmetric method of the beforeGroup() executed after the chunk processing
        // Note: if you don't need it you can delete it


    }

    @PreDestroy
    public void release() {
        // this is the symmetric method of the init() one,
        // release potential connections you created or data you cached
        // Note: if you don't need it you can delete it
    }
}