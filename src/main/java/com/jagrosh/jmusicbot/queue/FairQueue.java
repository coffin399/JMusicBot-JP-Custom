/*
 * Copyright 2018 John Grosh (jagrosh).
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
package com.jagrosh.jmusicbot.queue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @param <T>
 * @author John Grosh (jagrosh)
 */
public class FairQueue<T extends Queueable> {
    private final List<T> list = new ArrayList<>();
    private final Set<Long> set = new HashSet<>();

    /**
     * @deprecated 新しくフェアキューと普通のキューを切り替えられるメゾットを追加したのでそちらを使用してください。
     * @param item 追加する楽曲情報
     * @return 何曲目に追加したか
     */
    public int add(T item) {
        int lastIndex;
        for (lastIndex = list.size() - 1; lastIndex > -1; lastIndex--)
            if (list.get(lastIndex).getIdentifier() == item.getIdentifier())
                break;
        lastIndex++;
        set.clear();
        for (; lastIndex < list.size(); lastIndex++) {
            if (set.contains(list.get(lastIndex).getIdentifier()))
                break;
            set.add(list.get(lastIndex).getIdentifier());
        }
        list.add(lastIndex, item);
        return lastIndex;
    }

    /**
     * キューに楽曲を追加します。
     * @param item 楽曲情報
     * @param forceToEnd 強制的にキューの一番最後に追加するか
     * @return 何番目に追加したか
     */
    public int add(T item, boolean forceToEnd) {
        if (forceToEnd) {
            list.add(item);
            return list.size() - 1;
        }

        int lastIndex;
        for (lastIndex = list.size() - 1; lastIndex > -1; lastIndex--)
            if (list.get(lastIndex).getIdentifier() == item.getIdentifier())
                break;
        lastIndex++;
        set.clear();
        for (; lastIndex < list.size(); lastIndex++) {
            if (set.contains(list.get(lastIndex).getIdentifier()))
                break;
            set.add(list.get(lastIndex).getIdentifier());
        }
        list.add(lastIndex, item);
        return lastIndex;
    }

    public void addAt(int index, T item) {
        if (index >= list.size())
            list.add(item);
        else
            list.add(index, item);
    }

    public int size() {
        return list.size();
    }

    public T pull() {
        return list.remove(0);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public List<T> getList() {
        return list;
    }

    public T get(int index) {
        return list.get(index);
    }

    public T remove(int index) {
        return list.remove(index);
    }

    public int removeAll(long identifier) {
        int count = 0;
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).getIdentifier() == identifier) {
                list.remove(i);
                count++;
            }
        }
        return count;
    }

    public void clear() {
        list.clear();
    }

    public int shuffle(long identifier) {
        List<Integer> iset = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIdentifier() == identifier)
                iset.add(i);
        }
        for (int j = 0; j < iset.size(); j++) {
            int first = iset.get(j);
            int second = iset.get((int) (Math.random() * iset.size()));
            T temp = list.get(first);
            list.set(first, list.get(second));
            list.set(second, temp);
        }
        return iset.size();
    }

    public void skip(int number) {
        if (number > 0) {
            list.subList(0, number).clear();
        }
    }

    /**
     * アイテムをリスト内の別の位置に移動します
     *
     * @param from アイテムの位置
     * @param to   アイテムの新しい位置
     * @return the 移動したアイテム
     */
    public T moveItem(int from, int to) {
        T item = list.remove(from);
        list.add(to, item);
        return item;
    }
}
