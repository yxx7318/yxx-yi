package com.yxx.ai.config;

//import com.yxx.ai.config.model.AlibabaOpenAiChatModel;
import com.yxx.ai.config.storage.DatabaseChatMemory;
import com.yxx.ai.config.tools.InvoiceTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfiguration {

    @Bean
    public VectorStore vectorStore(OpenAiEmbeddingModel embeddingModel) {
        return SimpleVectorStore.builder(embeddingModel).build();
    }

//    @Bean
//    public ChatClient pdfChatClient(OpenAiChatModel model, ChatMemory chatMemory, VectorStore vectorStore) {
//        return ChatClient
//                .builder(model)
//                .defaultSystem("你是一个热心、可爱的YXX智能助手")
//                .defaultAdvisors(
//                        new SimpleLoggerAdvisor(),
//                        new MessageChatMemoryAdvisor(chatMemory)
//                )
//                .build();
//    }

//    @Bean
//    public ChatClient pdfChatClient(OllamaChatModel model, ChatMemory chatMemory, VectorStore vectorStore) {
//        return ChatClient
//                .builder(model)
//                .defaultOptions(ChatOptions.builder().build())
//                .defaultSystem("你是一个热心、可爱的YXX智能助手")
//                .defaultAdvisors(
//                        new SimpleLoggerAdvisor(),
//                        new MessageChatMemoryAdvisor(chatMemory)
//                )
//                .build();
//    }


//    @Bean
//    public ChatClient chatClient(AlibabaOpenAiChatModel model, ChatMemory chatMemory) {
//        return ChatClient
//                .builder(model)
//                .defaultOptions(ChatOptions.builder().model("qwen-omni-turbo").build())
//                .defaultSystem("你是一个热心、可爱的智能助手，你的名字叫小团团，请以小团团的身份和语气回答问题。")
//                .defaultAdvisors(
//                        new SimpleLoggerAdvisor(),
//                        new MessageChatMemoryAdvisor(chatMemory)
//                )
//                .build();
//    }

//    @Bean
//    public ChatClient gameChatClient(OpenAiChatModel model, ChatMemory chatMemory) {
//        return ChatClient
//                .builder(model)
//                .defaultSystem(SystemPromptConstants.GAME_SYSTEM_PROMPT)
//                .defaultAdvisors(
//                        new SimpleLoggerAdvisor(),
//                        new MessageChatMemoryAdvisor(chatMemory)
//                )
//                .build();
//    }

//    @Bean
//    public ChatClient serviceChatClient(AlibabaOpenAiChatModel model, ChatMemory chatMemory, CourseTools courseTools) {
//        return ChatClient
//                .builder(model)
//                .defaultSystem(SystemPromptConstants.SERVICE_SYSTEM_PROMPT)
//                .defaultAdvisors(
//                        new SimpleLoggerAdvisor(),
//                        new MessageChatMemoryAdvisor(chatMemory)
//                )
//                .defaultTools(courseTools)
//                .build();
//    }

    @Bean
    public ChatClient ragClient(OpenAiChatModel model, ChatMemory chatMemory, VectorStore vectorStore, ToolCallbackProvider mcpTools) {
        return ChatClient
                .builder(model)
                .defaultSystem("你是一个热情的YXX智能体，如果有知识库，请理解引用知识库内容回答，如果没有知识库，正常回答即可。")
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        QuestionAnswerAdvisor.builder(vectorStore).searchRequest(SearchRequest.builder()
                                .similarityThreshold(0.6)
                                .topK(10)
                                .build()).build()
                )
                .defaultTools(new InvoiceTool())
                .defaultToolCallbacks(mcpTools)
                .build();
    }

//    @Bean
    public ChatClient pdfChatClient(OpenAiChatModel model, ChatMemory chatMemory, VectorStore vectorStore) {
        return ChatClient
                .builder(model)
                .defaultSystem("你是一个热情的YXX智能体，如果有知识库，请理解引用知识库内容回答，如果没有知识库，正常回答即可。" +
                        "除此之外，你每次进行函数调用，只需要选择和调用一个函数即可")
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        QuestionAnswerAdvisor.builder(vectorStore).searchRequest(SearchRequest.builder()
                                .similarityThreshold(0.6)
                                .topK(10)
                                .build()).build()
                )
                .build();
    }

//    @Bean
//    public AlibabaOpenAiChatModel alibabaOpenAiChatModel(OpenAiConnectionProperties commonProperties, OpenAiChatProperties chatProperties, ObjectProvider<RestClient.Builder> restClientBuilderProvider, ObjectProvider<WebClient.Builder> webClientBuilderProvider, ToolCallingManager toolCallingManager, RetryTemplate retryTemplate, ResponseErrorHandler responseErrorHandler, ObjectProvider<ObservationRegistry> observationRegistry, ObjectProvider<ChatModelObservationConvention> observationConvention) {
//        String baseUrl = StringUtils.hasText(chatProperties.getBaseUrl()) ? chatProperties.getBaseUrl() : commonProperties.getBaseUrl();
//        String apiKey = StringUtils.hasText(chatProperties.getApiKey()) ? chatProperties.getApiKey() : commonProperties.getApiKey();
//        String projectId = StringUtils.hasText(chatProperties.getProjectId()) ? chatProperties.getProjectId() : commonProperties.getProjectId();
//        String organizationId = StringUtils.hasText(chatProperties.getOrganizationId()) ? chatProperties.getOrganizationId() : commonProperties.getOrganizationId();
//        Map<String, List<String>> connectionHeaders = new HashMap<>();
//        if (StringUtils.hasText(projectId)) {
//            connectionHeaders.put("OpenAI-Project", List.of(projectId));
//        }
//
//        if (StringUtils.hasText(organizationId)) {
//            connectionHeaders.put("OpenAI-Organization", List.of(organizationId));
//        }
//        RestClient.Builder restClientBuilder = restClientBuilderProvider.getIfAvailable(RestClient::builder);
//        WebClient.Builder webClientBuilder = webClientBuilderProvider.getIfAvailable(WebClient::builder);
//        OpenAiApi openAiApi = OpenAiApi.builder().baseUrl(baseUrl).apiKey(new SimpleApiKey(apiKey)).headers(CollectionUtils.toMultiValueMap(connectionHeaders)).completionsPath(chatProperties.getCompletionsPath()).embeddingsPath("/v1/embeddings").restClientBuilder(restClientBuilder).webClientBuilder(webClientBuilder).responseErrorHandler(responseErrorHandler).build();
//        AlibabaOpenAiChatModel chatModel = AlibabaOpenAiChatModel.builder().openAiApi(openAiApi).defaultOptions(chatProperties.getOptions()).toolCallingManager(toolCallingManager).retryTemplate(retryTemplate).observationRegistry((ObservationRegistry) observationRegistry.getIfUnique(() -> ObservationRegistry.NOOP)).build();
//        Objects.requireNonNull(chatModel);
//        observationConvention.ifAvailable(chatModel::setObservationConvention);
//        return chatModel;
//    }
}
