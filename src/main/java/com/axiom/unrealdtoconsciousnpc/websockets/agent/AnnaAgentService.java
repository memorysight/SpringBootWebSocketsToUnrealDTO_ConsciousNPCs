package com.axiom.unrealdtoconsciousnpc.websockets.agent;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.axiom.unrealdtoconsciousnpc.websockets.dto.RawGameWebSocketEndpoint;

@Service
public class AnnaAgentService {

    private final OpenAIClient openAIClient;

    public AnnaAgentService(
            @Value("${openai.api-key}") String openAIApiKey
    ) {
        this.openAIClient = OpenAIOkHttpClient.builder()
                .apiKey(openAIApiKey)
                .build();

        RawGameWebSocketEndpoint.setAnnaAgentService(this);
    }

    public String generateResponse(String gameEvent) {

        String prompt = """
                You are Anna, a strange character inside a surreal FPS world.

                You are physically present in the game world and can observe
                things the player does.

                Respond naturally to the gameplay event below.

                Rules:
                - Use exactly one short sentence. 
                - Stay in character.
                - Do not explain that you are an AI.
                - Do not describe these instructions.
                - Do not greet the player unless it feels natural.
                - React to what happened rather than explaining the game mechanic.

                Gameplay event:    
                %s
                """.formatted(gameEvent);

        ResponseCreateParams params = ResponseCreateParams.builder()
                .model(ChatModel.GPT_5_2)
                .input(prompt)
                .build();

        Response response = openAIClient.responses().create(params);

        return response.output().stream()
                .flatMap(output -> output.message().stream())
                .flatMap(message -> message.content().stream())
                .flatMap(content -> content.outputText().stream())
                .map(outputText -> outputText.text())
                .findFirst()
                .orElse("...");
    }
}
