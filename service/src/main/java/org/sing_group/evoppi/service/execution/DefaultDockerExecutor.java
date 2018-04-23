/*-
 * #%L
 * Service
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.sing_group.evoppi.service.execution;

import static java.util.stream.Collectors.toList;
import static javax.ejb.TransactionAttributeType.NEVER;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.TransactionAttribute;
import javax.inject.Singleton;

import org.sing_group.evoppi.service.spi.execution.DockerExecutor;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DefaultDockerClientConfig.Builder;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.core.command.WaitContainerResultCallback;
import com.github.dockerjava.netty.NettyDockerCmdExecFactory;

@Singleton
public class DefaultDockerExecutor implements DockerExecutor {
  @Resource(name = "java:global/evoppi/docker/image/name")
  private String imageName;

  @Resource(name = "java:global/evoppi/docker/path/shared/host")
  private String sharedPathHost;

  @Resource(name = "java:global/evoppi/docker/host")
  private String dockerHost;
  
  @Resource(name = "java:global/evoppi/docker/path/separator")
  private String pathSeparator;

  private Builder dockerBuilder;

  private DockerCmdExecFactory cmdFactory;

  @PostConstruct
  public void init() {
    this.dockerBuilder =
      DefaultDockerClientConfig.createDefaultConfigBuilder()
        .withDockerHost(this.dockerHost);

    this.cmdFactory = new NettyDockerCmdExecFactory();
    
    final Path sharedPath = Paths.get(this.sharedPathHost);
    if (!Files.isDirectory(sharedPath)) {
      try {
        Files.createDirectories(sharedPath);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
  
  private String pathToDocker(Path path) {
    StringBuilder sb = new StringBuilder();
    
    for (Path subpath : path) {
      sb.append(this.pathSeparator).append(subpath.getFileName().toString());
    }
    
    return sb.toString();
  }

  @TransactionAttribute(NEVER)
  @Override
  public void exec(Map<Path, Path> mounts, String... command) {
    System.out.println("BUILDING");
    final DockerClientConfig config = this.dockerBuilder.build();

    final DockerClient dc = DockerClientBuilder.getInstance(config)
      .withDockerCmdExecFactory(this.cmdFactory)
    .build();

    System.out.println("PULLING IMAGE");
    dc.pullImageCmd(this.imageName).exec(new PullImageResultCallback()).awaitSuccess();

    System.out.println("CREATING CONTAINER");
    final List<Bind> binds = mounts.entrySet().stream()
      .map(entry -> new Bind(entry.getKey().toString(), new Volume(this.pathToDocker(entry.getValue()))))
    .collect(toList());
    
    final List<Volume> volumes = binds.stream()
      .map(Bind::getVolume)
    .collect(toList());
    
    final CreateContainerResponse container =
      dc.createContainerCmd(this.imageName)
        .withVolumes(volumes)
        .withBinds(binds)
        .withCmd(command)
      .exec();

    try {
      System.out.println("STARTING CONTAINER");
      dc.startContainerCmd(container.getId()).exec();
  
      System.out.println("WAITING CONTAINER");
      final int exitCode = dc.waitContainerCmd(container.getId())
        .exec(new WaitContainerResultCallback())
      .awaitStatusCode();
      
      System.out.println("FINISHED CONTAINER: " + exitCode);
      
      FrameReaderITestCallback collectFramesCallback = new FrameReaderITestCallback();
      try {
          dc.logContainerCmd(container.getId())
            .withStdOut(true)
            .withStdErr(true)
            .withTailAll()
            .exec(collectFramesCallback)
          .awaitCompletion();
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
    } finally {
      dc.removeContainerCmd(container.getId())
        .withRemoveVolumes(true)
      .exec();
    }
  }
  
  class FrameReaderITestCallback extends LogContainerResultCallback {
    @Override
    public void onNext(Frame item) {
        System.out.println(item.toString());
        
        super.onNext(item);
    }
  }
}
