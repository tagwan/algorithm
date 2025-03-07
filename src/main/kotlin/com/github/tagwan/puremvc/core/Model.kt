//
//  Model.kt
//  PureMVC Kotlin Multicore
//
//  Copyright(c) 2020 Saad Shams <saad.shams@puremvc.org>
//  Your reuse is governed by the Creative Commons Attribution 3.0 License
//

package com.github.tagwan.puremvc.core

import com.github.tagwan.puremvc.interfaces.IModel
import com.github.tagwan.puremvc.interfaces.IProxy
import java.util.concurrent.ConcurrentHashMap

/**
 * <P>A Multiton <code>IModel</code> implementation.</P>
 *
 * <P>In PureMVC, the <code>Model</code> class provides
 * access to model objects (Proxies) by named lookup.</P>
 *
 * <P>The <code>Model</code> assumes these responsibilities:</P>
 *
 * <UL>
 * <LI>Maintain a cache of <code>IProxy</code> instances.</LI>
 * <LI>Provide methods for registering, retrieving, and removing
 * <code>IProxy</code> instances.</LI>
 * </UL>
 *
 * <P>Your application must register <code>IProxy</code> instances
 * with the <code>Model</code>. Typically, you use an
 * <code>ICommand</code> to create and register <code>IProxy</code>
 * instances once the <code>Facade</code> has initialized the Core
 * actors.</p>
 *
 * @see com.github.tagwan.puremvc.patterns.proxy.Proxy Proxy
 * @see com.github.tagwan.puremvc.interfaces.IProxy IProxy
 *
 * @constructor Creates a Model
 */
open class Model(key: String) : IModel {

    companion object {
        // Message Constants
        const val MULTITON_MSG = "Model instance for this Multiton key already constructed!"

        // The Multiton Model instanceMap.
        private val instanceMap = ConcurrentHashMap<String, IModel>()

        /**
         * <P><code>Model</code> Multiton Factory method.</P>
         *
         * @param key multitonKey
         * @param factory factory that returns <code>IModel</code>
         * @return the Multiton instance of <code>Model</code>
         */
        @Synchronized
        fun getInstance(key: String, factory: (key: String) -> IModel): IModel {
            return instanceMap.getOrPut(key) { factory(key) }
        }

        /**
         * <P>Remove an IModel instance</P>
         *
         * @param key of IModel instance to remove
         */
        @Synchronized
        fun removeModel(key: String) {
            instanceMap.remove(key)
        }
    }

    // The Multiton Key for this Core
    protected var multitonKey: String

    // Mapping of proxyNames to IProxy instances
    protected val proxyMap = ConcurrentHashMap<String, IProxy>()

    /**
     * <P>Constructor.</P>
     *
     * <P>This <code>IModel</code> implementation is a Multiton,
     * so you should not call the constructor
     * directly, but instead call the static Multiton</P>
     *
     * Factory method {@code Model.getInstance(multitonKey, () -> new Model(multitonKey))}
     *
     * @param key multitonKey
     * @throws Error Error if instance for this Multiton key instance has already been constructed
     *
     */
    init {
        if (instanceMap.containsKey(key)) throw Error(MULTITON_MSG)
        multitonKey = key
        instanceMap[key] = this;
        this.initializeModel()
    }

    /**
     * <P>Initialize the <code>Model</code> instance.</P>
     *
     * <P>Called automatically by the constructor, this
     * is your opportunity to initialize the Singleton
     * instance in your subclass without overriding the
     * constructor.</P>
     */
    override fun initializeModel() {

    }

    /**
     * <P>Register an <code>IProxy</code> with the <code>Model</code>.</P>
     *
     * @param proxy an <code>IProxy</code> to be held by the <code>Model</code>.
     */
    override fun registerProxy(proxy: IProxy) {
        proxy.run {
            initializeNotifier(multitonKey)
            proxyMap[proxy.name] = this
            onRegister()
        }
    }

    /**
     * <P>Retrieve an <code>IProxy</code> from the <code>Model</code>.</P>
     *
     * @param proxyName proxy name
     * @return the <code>IProxy</code> instance previously registered with the given <code>proxyName</code>.
     */
    override fun retrieveProxy(proxyName: String): IProxy? {
        return proxyMap[proxyName]
    }

    /**
     * <P>Check if a Proxy is registered</P>
     *
     * @param proxyName proxy name
     * @return whether a Proxy is currently registered with the given <code>proxyName</code>.
     */
    override fun hasProxy(proxyName: String): Boolean {
        return proxyMap.containsKey(proxyName)
    }

    /**
     * <P>Remove an <code>IProxy</code> from the <code>Model</code>.</P>
     *
     * @param proxyName name of the <code>IProxy</code> instance to be removed.
     * @return the <code>IProxy</code> that was removed from the <code>Model</code>
     */
    override fun removeProxy(proxyName: String): IProxy? {
        return proxyMap[proxyName]?.let { proxy ->
            proxyMap.remove(proxyName)
            proxy.onRemove()
            return proxy
        }
    }

}