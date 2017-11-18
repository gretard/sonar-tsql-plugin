package org.sonar.plugins.tsql.helpers;

import java.util.Set;
import java.util.TreeSet;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.reflections.Reflections;

public class ClassesLister {

	public static void main(String[] args) {
		Reflections reflections = new Reflections("org.antlr.tsql");
		Set<Class<? extends ParserRuleContext>> subTypes = reflections.getSubTypesOf(ParserRuleContext.class);
		TreeSet<String> ordered = new TreeSet<>();
		for (@SuppressWarnings("rawtypes") Class c : subTypes) {
			ordered.add(c.getSimpleName());
		}
		for (String x : ordered) {
			System.out.println("- " + x);
		}
		System.out.println(ordered.size());
	}

}
