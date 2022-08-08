/**
* Create the UMB message body for OpenShift Serverless Logic and invoke prod/send_umb_message job to send the message.
*/
def scriptTemplate = this.getClass().getResource("job-scripts/prod_osl_trigger_umb_message.jenkinsfile").text
def parsedScript = scriptTemplate.replaceAll(/<%=\s*(\w+)\s*%>/) { config[it[1]] ?: '' }

def folderPath = 'PROD'
folder(folderPath)

pipelineJob("${folderPath}/osl-trigger-umb-message") {
    description('This job creates the OSL UMB message body and invoke prod/send_umb_message job to send the message.')

    parameters {
        stringParam('PRODUCT_ID', '', 'The product ID. This parameter is optional and in case it is not defined, the default OSL product ID is used.')
        stringParam('MILESTONE', '', 'The release milestone, i.e. 1.24.0.CR1. This parameter is optional and in case it is not defined, the latest available milestone is used.')
        stringParam("PNC_API_URL", "http://orch.psi.redhat.com/pnc-rest/v2", "PNC Rest API endpoint. See: https://docs.engineering.redhat.com/display/JP/User%27s+guide")
    }

    logRotator {
        numToKeep(32)
    }

    definition {
        cps {
            script(parsedScript)
            sandbox()
        }
    }
}