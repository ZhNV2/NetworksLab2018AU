package server;

import http.HttpRequest;
import http.HttpResponse;
import server.commandrunner.CommandRunner;
import server.commandrunner.NotImplementedCommandRunner;
import utils.API;
import utils.NotImplementedException;
import utils.request.RequestCommand;
import utils.response.ResponseCommand;

import java.net.Socket;

public class Logic {
    private final Context context;
    private final Network network;
    private volatile boolean running;

    public Logic(Context context, Socket clientSocket) {
        running = true;
        this.context = context;
        network = new Network(clientSocket);
        network.start();
    }

    public void stop() {
        running = false;
    }

    public String process(RequestCommand requestCommand) {
        CommandRunner commandRunner = requestCommand.getAPI().getCommandRunner();
        ResponseCommand responseCommand = commandRunner.run(requestCommand, context);
        HttpResponse httpResponse = responseCommand.toHttpResponse();
        network.send(httpResponse);
        return responseCommand.getExecutionResult();
    }

    public void start() {
        while (running) {
            HttpRequest httpRequest = network.receive();
            RequestCommand requestCommand;
            try {
                requestCommand = API.buildAPI(
                        httpRequest.getMethod(),
                        httpRequest.getURI().getPath().split("/")[0] //TODO check
                ).buildRequest(httpRequest);
            } catch (NotImplementedException e) {
                ResponseCommand responseCommand = NotImplementedCommandRunner.getInstance().run();
                HttpResponse httpResponse = responseCommand.toHttpResponse();
                network.send(httpResponse);
                System.out.println(responseCommand.getExecutionResult());
                continue;
            }
            String executionResult = process(requestCommand);
            System.out.println(executionResult);
        }
    }
}
