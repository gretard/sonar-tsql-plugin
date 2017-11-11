package org.sonar.plugins.tsql.sensors.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sonar.plugins.tsql.rules.custom.RuleImplementation;
import org.sonar.plugins.tsql.sensors.custom.matchers.RulesMatcher;
import org.sonar.plugins.tsql.sensors.custom.nodes.IParsedNode;

public class NodesMatchingRulesProvider {

	public Map<RuleImplementation, List<IParsedNode>> check(RuleImplementation rule, IParsedNode node) {

		Map<RuleImplementation, List<IParsedNode>> map = new HashMap<RuleImplementation, List<IParsedNode>>();
		initMap(map, rule);
		visit(node, null, rule, map);

		return map;
	}

	RulesMatcher matcher = new RulesMatcher();

	void visit(IParsedNode node, IParsedNode parent, RuleImplementation rule,
			Map<RuleImplementation, List<IParsedNode>> items) {
		if (this.matcher.match(rule, parent, node)) {
			System.out.println("FOUND MATCH: "+node.getText()+" "+rule.getRuleMatchType()+" CC: "+node.getChildren().size());
			if (!items.get(rule).contains(node)) {
				items.get(rule).add(node);
			}

			for (final IParsedNode nnode : node.getSiblings()) {
				
				for (final RuleImplementation vRule : rule.getSiblingsRules().getRuleImplementation()) {
					System.out.println("SIBLING: "+nnode.getClassName()+" "+nnode.getText()+" "+vRule.getRuleMatchType());
					
					visit(nnode, node, vRule, items);
				}
			}
			for (final IParsedNode nnode : node.getChildren()) {
				for (final RuleImplementation vRule : rule.getChildrenRules().getRuleImplementation()) {
					System.out.println("CHILD: "+nnode.getClassName()+" "+nnode.getText()+" "+vRule.getRuleMatchType());
					
					visit(nnode, node, vRule, items);
				}
			}
			for (final IParsedNode nnode : node.getParents()) {
				
				for (final RuleImplementation vRule : rule.getParentRules().getRuleImplementation()) {
					System.out.println("PARENTS: "+nnode.getClassName()+" "+nnode.getText()+" "+vRule.getRuleMatchType());
					
					visit(nnode, node, vRule, items);
				}
			}
		}
	}

	void initMap(Map<RuleImplementation, List<IParsedNode>> map, RuleImplementation rule) {
		map.putIfAbsent(rule, new ArrayList<>());

		for (RuleImplementation i : rule.getChildrenRules().getRuleImplementation()) {
			initMap(map, i);
		}
		for (RuleImplementation i : rule.getParentRules().getRuleImplementation()) {
			initMap(map, i);
		}
		for (RuleImplementation i : rule.getUsesRules().getRuleImplementation()) {
			initMap(map, i);
		}
		for (RuleImplementation i : rule.getParentRules().getRuleImplementation()) {
			initMap(map, i);
		}
	}
}
