package me.reb4ck.smp.base.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CopyArchiveToContainerCmd;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.reb4ck.smp.api.service.DockerService;
import me.reb4ck.smp.utils.Console;
import me.reb4ck.smp.server.SMPServer;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

@Singleton
public final class DockerServiceImpl implements DockerService {
    private final DockerClient dockerClient;

    @Inject
    public DockerServiceImpl() {
        this.dockerClient = setupClient();
    }

    @Override
    public void deleteDocker(String smpServer) {
        if(!containerExists(smpServer)) return;

        dockerClient
                .removeContainerCmd(smpServer)
                .withForce(true)
                .exec();
    }



    @Override
    public void createDocker(SMPServer smpServer) {

        if(containerExists(smpServer.getName())) return;

    }

    @Override
    public void startDocker(SMPServer smpServer) {
        if(isRunning(smpServer.getName()))
            Console.sendMessage("&c[SMP] Someone tried to start container " + smpServer.getName() + " but it's already started!");
        else
            dockerClient.startContainerCmd(smpServer.getName()).exec();
    }

    @Override
    public void stopDocker(SMPServer smpServer) {
        if(!isRunning(smpServer.getName()))
            Console.sendMessage("&c[SMP] Someone tried to stop container " + smpServer.getName() + " but it's already stoped!");
        else
            dockerClient.stopContainerCmd(smpServer.getName()).exec();
    }

    @Override
    public void copyFilesToContainer(String container, String source, String destine) {
        CopyArchiveToContainerCmd copyArchiveToContainerCmd = dockerClient.copyArchiveToContainerCmd(container)
                .withHostResource(source)
                .withDirChildrenOnly(true)
                .withRemotePath(destine);

        copyArchiveToContainerCmd.exec();
        copyArchiveToContainerCmd.exec();
    }

    @Override
    public void restartDocker(String container) {
        dockerClient.restartContainerCmd(container).exec();
    }

    @Override
    public boolean containerExists(String name) {
        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(name).exec();

        return inspectContainerResponse.toString() != null && inspectContainerResponse.toString().length() > 0;
    }

    @Override
    public boolean isRunning(String name) {
        try {
            InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(name).exec();

            if(inspectContainerResponse.getState().getRunning() == null) return false;

            return inspectContainerResponse.getState().getRunning();
        }catch (Exception e){

            Console.sendMessage("Failed checking is container " + name + " is running! " + e.getMessage());

            e.printStackTrace();

            return false;
        }
    }

    @Override
    public void close() {
        Optional.ofNullable(dockerClient).ifPresent(client -> {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private DockerClient setupClient(){

        DockerClientConfig standard = DefaultDockerClientConfig
                .createDefaultConfigBuilder()
                .build();
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(standard.getDockerHost())
                .sslConfig(standard.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();
        return DockerClientImpl.getInstance(standard, httpClient);
    }
}
