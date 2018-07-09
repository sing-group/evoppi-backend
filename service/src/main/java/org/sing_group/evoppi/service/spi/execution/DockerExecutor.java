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

package org.sing_group.evoppi.service.spi.execution;

import static java.util.Collections.emptyMap;

import java.nio.file.Path;
import java.util.Map;

public interface DockerExecutor {
  public default void exec(String ... command) {
    this.exec(emptyMap(), command);
  }
  
  public default void exec(String command) {
    this.exec(emptyMap(), command.split("\\s+"));
  }
  public void exec(Map<Path, Path> mounts, String ... command);
  
  public default void exec(Map<Path, Path> mounts, String command) {
    this.exec(mounts, command.split("\\s+"));
  }
}
