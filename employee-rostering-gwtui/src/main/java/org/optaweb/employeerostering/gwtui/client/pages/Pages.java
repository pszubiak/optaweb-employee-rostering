/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaweb.employeerostering.gwtui.client.pages;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.optaweb.employeerostering.gwtui.client.admin.AdminPage;
import org.optaweb.employeerostering.gwtui.client.employee.EmployeeListPanel;
import org.optaweb.employeerostering.gwtui.client.skill.SkillListPanel;
import org.optaweb.employeerostering.gwtui.client.spot.SpotListPanel;
import org.optaweb.employeerostering.gwtui.client.viewport.availabilityroster.AvailabilityRosterPage;
import org.optaweb.employeerostering.gwtui.client.viewport.rotation.RotationPage;
import org.optaweb.employeerostering.gwtui.client.viewport.shiftroster.ShiftRosterPage;

import static org.optaweb.employeerostering.gwtui.client.pages.Pages.Id.*;

@Dependent
public class Pages {

    public enum Id {
        SHIFT_ROSTER,
        AVAILABILITY_ROSTER,
        ROTATION,
        SKILLS,
        SPOTS,
        EMPLOYEES,
        ADMIN;
    }

    @Inject
    private ManagedInstance<ShiftRosterPage> shiftRosterPage;

    @Inject
    private ManagedInstance<RotationPage> rotationPage;

    @Inject
    private ManagedInstance<SkillListPanel> skillsPage;

    @Inject
    private ManagedInstance<SpotListPanel> spotsPage;

    @Inject
    private ManagedInstance<EmployeeListPanel> employeesPage;

    @Inject
    private ManagedInstance<AvailabilityRosterPage> availabilityRosterPage;

    @Inject
    private ManagedInstance<AdminPage> adminPage;

    private final Map<Id, LazyInit<? extends Page>> mapping = new HashMap<>();

    @PostConstruct
    public void init() {
        mapping.put(SKILLS, lazyInit(skillsPage));
        mapping.put(SPOTS, lazyInit(spotsPage));
        mapping.put(EMPLOYEES, lazyInit(employeesPage));
        mapping.put(SHIFT_ROSTER, lazyInit(shiftRosterPage));
        mapping.put(AVAILABILITY_ROSTER, lazyInit(availabilityRosterPage));
        mapping.put(ROTATION, lazyInit(rotationPage));
        mapping.put(ADMIN, lazyInit(adminPage));
    }

    public Page get(final Id id) {
        //FIXME: Improve error handling?
        return Optional.ofNullable(mapping.get(id)).map(s -> s.get()).orElseThrow(() -> new RuntimeException("Unmapped page " + id));
    }

    private <T> LazyInit<T> lazyInit(final ManagedInstance<T> managedInstance) {
        return new LazyInit<>(managedInstance);
    }

    private static class LazyInit<T> {

        private final ManagedInstance<T> managedInstance;

        private T instance;

        private LazyInit(final ManagedInstance<T> managedInstance) {
            this.managedInstance = managedInstance;
        }

        public T get() {

            if (instance == null) {
                instance = managedInstance.get();
            }

            return instance;
        }
    }
}
