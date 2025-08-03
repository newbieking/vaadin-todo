package org.example

import com.github.mvysny.vaadinboot.VaadinBoot
import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.component.page.Push
import com.vaadin.flow.shared.communication.PushMode
import com.vaadin.flow.theme.Theme
import org.example.models.DatabaseFactory

//@Theme("vaadin-app")
@Push(PushMode.AUTOMATIC) // websocket ui updates
class AppShell : AppShellConfigurator


fun main() {
    DatabaseFactory.init()
    VaadinBoot().run()
}