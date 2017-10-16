package fr.pe.gssi.progiciel.oi043.plugins.oi043ascode.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface DevTestProxySLDV4 {
	String devTestAgents();
	String port();
	String environnement();
	String configLD();

}
