//
//  ICommand.kt
//  PureMVC Kotlin Multicore
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package com.github.tagwan.puremvc.interfaces

/**
 * The interface definition for a PureMVC Command.
 *
 * @see com.github.tagwan.puremvc.interfaces INotification
 */
interface ICommand : INotifier {

    /**
     * <P>Execute the <code>ICommand</code>'s logic to handle a given <code>INotification</code>.</P>
     *
     * @param notification an <code>INotification</code> to handle.
     */
    fun execute(notification: INotification)

}