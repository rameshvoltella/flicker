package com.ua.max.oliynick.flicker.interfaces;

import com.ua.max.oliynick.flicker.util.LastChatModel;

import java.util.Comparator;

/**
 * <p>
 *     Controller for last conversation fragment
 * </p>
 * Created by Максим on 23.02.2016.
 */
public interface ILastChatsController {

    void onFilter(String input);

    void onSort(Comparator<LastChatModel> comparator);

}
