/*
 * Copyright (C) 2018 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.openshift.employeerostering.gwtui.client.beta.java.powers;

import java.util.function.BiConsumer;

import elemental2.dom.HTMLElement;
import org.jboss.errai.common.client.api.elemental2.IsElement;
import org.optaplanner.openshift.employeerostering.gwtui.client.beta.java.list.ListView;
import org.optaplanner.openshift.employeerostering.gwtui.client.beta.java.model.Viewport;
import org.optaplanner.openshift.employeerostering.gwtui.client.pages.rotation.ShiftBlobView;

public class CircularResizability<T, Y extends BlobWithTwin<T, Y>> {

    private Y blob;
    private Viewport<T> viewport;
    private BlobChangeHandler<T, Y> handler;

    public void applyFor(final ListView<Y> list,
                         final IsElement blobView,
                         final ShiftBlobView.CollisionDetector<Y> collisionDetector,
                         final Viewport<T> viewport,
                         final Y blob) {

        this.viewport = viewport;
        this.blob = blob;
        this.handler = new BlobChangeHandler<>(blob, viewport, collisionDetector, list);

        makeResizable(blobView.getElement(),
                      viewport.getGridPixelSizeInScreenPixels().intValue(),
                      viewport.decideBasedOnOrientation("s", "e"));
    }

    private native void makeResizable(final HTMLElement blob,
                                      final int pixelSize,
                                      final String orientation) /*-{
        var that = this;
        var $blob = $wnd.jQuery(blob);

        var snapToGrid = function (coordinate) {
            return Math.floor(coordinate - (coordinate % pixelSize))
        };

        $blob.resizable({
            handles: orientation,
            minHeight: 0,
            resize: function (e, ui) {
                if (orientation === 's') {
                    ui.size.height = snapToGrid(ui.size.height + 2 * pixelSize);
                } else if (orientation === 'e') {
                    ui.size.width = snapToGrid(ui.size.width + 2 * pixelSize);
                }
                that.@org.optaplanner.openshift.employeerostering.gwtui.client.beta.java.powers.CircularResizability::onResize(II)(ui.size.height, ui.size.width);
            }
        });
    }-*/;

    private boolean onResize(final int height, final int width) {
        final Long newSizeInScreenPixels = viewport.decideBasedOnOrientation(height, width).longValue();
        final Long newSizeInGridPixels = viewport.toGridPixels(newSizeInScreenPixels);

        if (!newSizeInGridPixels.equals(blob.getSizeInGridPixels())) {
            handler.handle(blob.getPositionInGridPixels(), newSizeInGridPixels);
        }

        return true;
    }

    public void onResize(final BiConsumer<Long, CollisionState> onResize) {
        handler.onChange(onResize);
    }
}
