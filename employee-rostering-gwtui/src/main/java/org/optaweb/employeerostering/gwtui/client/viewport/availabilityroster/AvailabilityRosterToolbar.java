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

package org.optaweb.employeerostering.gwtui.client.viewport.availabilityroster;

import org.jboss.errai.ui.client.local.api.elemental2.IsElement;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.optaweb.employeerostering.gwtui.client.common.EventManager.Event;
import org.optaweb.employeerostering.gwtui.client.common.LocalDateRange;
import org.optaweb.employeerostering.gwtui.client.viewport.RosterToolbar;
import org.optaweb.employeerostering.shared.roster.Pagination;
import org.optaweb.employeerostering.shared.roster.view.AvailabilityRosterView;

import static org.optaweb.employeerostering.gwtui.client.common.EventManager.Event.*;

@Templated
public class AvailabilityRosterToolbar extends RosterToolbar implements IsElement {

    @Override
    protected Event<AvailabilityRosterView> getViewRefreshEvent() {
        return AVAILABILITY_ROSTER_UPDATE;
    }

    @Override
    protected Event<Pagination> getPageChangeEvent() {
        return AVAILABILITY_ROSTER_PAGINATION;
    }

    @Override
    protected Event<Void> getViewInvalidateEvent() {
        return AVAILABILITY_ROSTER_INVALIDATE;
    }

    @Override
    protected Event<LocalDateRange> getDateRangeEvent() {
        return AVAILABILITY_ROSTER_DATE_RANGE;
    }

}
