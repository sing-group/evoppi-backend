/*-
 * #%L
 * REST
 * %%
 * Copyright (C) 2017 - 2022 Noé Vázquez González, Miguel Reboiro-Jato, Jorge Vieira, Hugo López-Fernández, 
 * 		and Cristina Vieira
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
package org.sing_group.evoppi.rest.resource;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.sing_group.evoppi.rest.resource.bio.DifferentSpeciesInteractionResourceIntegrationTest;
import org.sing_group.evoppi.rest.resource.bio.DifferentSpeciesInteractionResourceMissingQueryGene12vs34InteractomeIntegrationTest;
import org.sing_group.evoppi.rest.resource.bio.DifferentSpeciesInteractionResourceMissingQueryGene1vs3InteractomeIntegrationTest;
import org.sing_group.evoppi.rest.resource.bio.DifferentSpeciesInteractionResourceMissingQueryGene1vs4InteractomeIntegrationTest;
import org.sing_group.evoppi.rest.resource.bio.DifferentSpeciesInteractionResourceMissingQueryGene2vs3InteractomeIntegrationTest;
import org.sing_group.evoppi.rest.resource.bio.DifferentSpeciesInteractionResourceMissingQueryGene2vs4InteractomeIntegrationTest;
import org.sing_group.evoppi.rest.resource.bio.DifferentSpeciesInteractionResourceMultipleAlignmentIntegrationTest;
import org.sing_group.evoppi.rest.resource.bio.SameSpeciesInteractionResourceIntegrationTest;
import org.sing_group.evoppi.rest.resource.user.AdministratorResourceIntegrationTest;
import org.sing_group.evoppi.rest.resource.user.RegistrationResourceIntegrationTest;
import org.sing_group.evoppi.rest.resource.user.ResearcherResourceIntegrationTest;
import org.sing_group.evoppi.rest.resource.user.UserResourceIntegrationTest;

@SuiteClasses({
	UserResourceIntegrationTest.class,
	AdministratorResourceIntegrationTest.class,
	ResearcherResourceIntegrationTest.class,
	RegistrationResourceIntegrationTest.class,
	SameSpeciesInteractionResourceIntegrationTest.class,
	DifferentSpeciesInteractionResourceIntegrationTest.class,
	DifferentSpeciesInteractionResourceMissingQueryGene1vs3InteractomeIntegrationTest.class,
	DifferentSpeciesInteractionResourceMissingQueryGene1vs4InteractomeIntegrationTest.class,
	DifferentSpeciesInteractionResourceMissingQueryGene2vs3InteractomeIntegrationTest.class,
	DifferentSpeciesInteractionResourceMissingQueryGene2vs4InteractomeIntegrationTest.class,
	DifferentSpeciesInteractionResourceMissingQueryGene12vs34InteractomeIntegrationTest.class,
	DifferentSpeciesInteractionResourceMultipleAlignmentIntegrationTest.class
})
@RunWith(Suite.class)
public class ResourceIntegrationTestSuite {
}
