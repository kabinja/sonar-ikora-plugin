package lu.uni.serval.ikora.sonar.rules;

import lu.uni.serval.ikora.core.model.SourceFile;
import lu.uni.serval.ikora.core.model.SourceNode;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.checks.SensitiveLocatorCheck;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

import java.util.Set;

@Rule(key = SensitiveLocatorRule.RULE_KEY)
public class SensitiveLocatorRule extends IkoraLintRule {
    public static final String RULE_KEY = "sensitive-locator-rule";
    private static final Logger LOG = Loggers.get(SensitiveLocatorRule.class);

    @Override
    public void validate() {
        super.validate();

        final SourceFile sourceFile = ikoraSourceCode.getSourceFile();
        final SensitiveLocatorCheck check = new SensitiveLocatorCheck();
        final Set<SourceNode> nodes = check.collectInstances(sourceFile, new SmellConfiguration());

        for(SourceNode node: nodes){
            LOG.debug(String.format("Add issue '%s' found in '%s'", RULE_KEY, node.getSourceFile().getName()));

            IkoraIssue issue = new IkoraIssue(ruleKey,
                    "Complex locators lead to fragile tests as they are more prone to locator breakage",
                    node.getDefinitionToken().getLine(),
                    node.getDefinitionToken().getStartOffset(),
                    node.getDefinitionToken().getEndOffset()
            );

            ikoraSourceCode.addViolation(issue);
        }
    }
}
