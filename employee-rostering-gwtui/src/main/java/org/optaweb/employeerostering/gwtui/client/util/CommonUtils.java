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

package org.optaweb.employeerostering.gwtui.client.util;

import java.util.Collection;
import java.util.Iterator;
import javax.inject.Singleton;

import org.optaweb.employeerostering.gwtui.client.common.OneWayMapping;

@Singleton
public class CommonUtils {

    public int stringWithIntCompareTo(String a, String b) {
        Iterator<Integer> aIter = a.toLowerCase().chars().iterator();
        Iterator<Integer> bIter = b.toLowerCase().chars().iterator();

        while (true) {
            if (!aIter.hasNext() && !bIter.hasNext()) {
                return 0;
            } else if (!aIter.hasNext()) {
                return -1;
            } else if (!bIter.hasNext()) {
                return 1;
            }

            int aChar = aIter.next();
            int bChar = bIter.next();

            if (isDigit(aChar)) {
                if (isDigit(bChar)) {
                    char[] aValue = Character.toChars(aChar);
                    char[] bValue = Character.toChars(bChar);
                    StringBuilder aNum = new StringBuilder();
                    StringBuilder bNum = new StringBuilder();
                    boolean aHasChar = false;
                    boolean bHasChar = false;

                    for (char c : aValue) {
                        aNum.append(c);
                    }
                    for (char c : bValue) {
                        bNum.append(c);
                    }

                    while (aIter.hasNext()) {
                        aChar = aIter.next();
                        aValue = Character.toChars(aChar);
                        if (!isDigit(aChar)) {
                            aHasChar = true;
                            break;
                        }
                        for (char c : aValue) {
                            aNum.append(c);
                        }
                    }
                    while (bIter.hasNext()) {
                        bChar = bIter.next();
                        bValue = Character.toChars(bChar);
                        if (!isDigit(aChar)) {
                            bHasChar = true;
                            break;
                        }
                        for (char c : aValue) {
                            bNum.append(c);
                        }
                    }
                    int aInt = Integer.parseInt(aNum.toString());
                    int bInt = Integer.parseInt(bNum.toString());
                    if (aInt != bInt) {
                        return Integer.compare(aInt, bInt);
                    }
                    if (aHasChar && bHasChar) {
                        if (Integer.compare(aChar, bChar) != 0) {
                            return Integer.compare(aChar, bChar);
                        }
                    } else if (aHasChar) {
                        return 1;
                    } else if (bHasChar) {
                        return -1;
                    } else {
                        return 0;
                    }

                } else {
                    return Integer.compare(aChar, bChar);
                }
            } else {
                if (Integer.compare(aChar, bChar) != 0) {
                    return Integer.compare(aChar, bChar);
                }
            }
        }
    }

    public boolean isDigit(int a) {
        char[] chars = Character.toChars(a);
        return chars.length == 1 && Character.isDigit(chars[0]);
    }

    public String pad(String str, int len) {
        StringBuilder out = new StringBuilder(str);
        while (out.length() < len) {
            out.insert(0, "0");
        }
        return out.toString();
    }

    public int roundToNearestMultipleOf(double toRound, int num) {
        return (int) (Math.round(Math.round(toRound) * (1.0 / num)) * num);
    }

    public <T> String delimitCollection(Collection<T> toDelimit,
                                        OneWayMapping<T, String> mapper,
                                        String delimiter) {
        StringBuilder out = new StringBuilder();
        if (null == toDelimit) {
            return "";
        }
        for (T item : toDelimit) {
            out.append(mapper.map(item)).append(delimiter);
        }
        out.delete(out.length() - delimiter.length(), out.length());
        return out.toString();
    }

    public <T> Iterable<T> flatten(Iterable<? extends Iterable<T>> collection) {
        return new Iterable<T>() {

            @Override
            public Iterator<T> iterator() {
                return new FlattenIterator<>(collection);
            }
        };
    }

    private class FlattenIterator<T> implements Iterator<T> {

        Iterator<? extends Iterable<T>> mainIterator;
        Iterator<T> subIterator;

        public FlattenIterator(Iterable<? extends Iterable<T>> iterable) {
            mainIterator = iterable.iterator();
            if (mainIterator.hasNext()) {
                subIterator = mainIterator.next().iterator();
            }
        }

        @Override
        public boolean hasNext() {
            if (null == subIterator) {
                return false;
            }

            while (!subIterator.hasNext() && mainIterator.hasNext()) {
                subIterator = mainIterator.next().iterator();
            }
            return subIterator.hasNext();
        }

        @Override
        public T next() {
            return subIterator.next();
        }

    }
}
