package com.softlayer.api.automation.systest.functional.vsi

import groovy.json.JsonSlurper
import groovyx.net.http.RESTClient
import wslite.soap.*
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import utility.InputProvider;
import groovyx.net.http.ContentType
import utility.Time;

@Time
class RestSpecification extends Specification {

	
    RESTClient restClient = new RESTClient("https://api.softlayer.com/rest/v3.1/")
	SOAPClient soapClient = new SOAPClient('https://api.softlayer.com/soap/v3.1/SoftLayer_Account')
	
	@Shared provider = new InputProvider()
	
	def setup() {		
		restClient.auth.basic "sl307608-rcabero", "9f123905f8121d6862ea64630a3bb586f7900be12346deec1341d3e91e745528"
	} 
	
	@Time
    def 'Chech getObject'() {

        when:
        def response = restClient.get(path:"SoftLayer_Account/getObject")
		def response2 = restClient.get(path:"SoftLayer_Account/getHardware")
		def user = "sl307608-rcabero"
		def xml = """<soapenv:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:v3="http://api.service.softlayer.com/soap/v3/">
  <soapenv:Header>
    <authenticate xsi:type="v3:authenticate">
      <username xsi:type="xsd:string">${user}</username>
      <apiKey xsi:type="xsd:string">9f123905f8121d6862ea64630a3bb586f7900be12346deec1341d3e91e745528</apiKey>
    </authenticate>
  </soapenv:Header>
  <soapenv:Body>
    <v3:getHardware soapenv:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/"/>
  </soapenv:Body>
</soapenv:Envelope>"""
        

		def responseSoap = soapClient.send(xml)


        then:
        response.status == 200
		responseSoap.getHardwareResponse.getHardwareReturn.item[20].id == 438526
		

		and:
        response.data.country == 'US'
		response2.data[0].domain == 'open-test.ibmcloud.com'
        
    }
/*
    //@Unroll("Check #city matches #expectedResult")
    def 'Check if we can find multiple cities'() {
		println "[INFO] starting test Check if we can find multiple cities"
        when:
        def response = restClient.get( path: 'SoftLayer_Account/getHardware',
			query: [objectMask : "mask[billingItem]", 
			objectFilter: '{"hardware": {"id": {"operation" : 438792}}}'])
		
        then:
        response.status == 200
		println "[INFO] the value of the status ${response.status}"
		response.data[0].users?.name == 438792
        response.data[0].billingItem.id == 80428917
		println "[INFO] Ending test Check if we can find multiple cities"

    }
	
	@Unroll("Check #id matches #accountID")
	def 'Check csv provider'() {
		
		expect: 'value #id'
		def idInt = id as int
		def accountIdInt = accountID as int
		def totalInt = total as int
		idInt + accountIdInt == totalInt + 2
		
		where: 'value #accountID'
		[id, accountID, total] << provider.readCsv("test.csv")
	}
	
	@Unroll("Check #id matches #accountID")
	def 'Check csv provider 2'() {
		
		expect: 'value #id'
		def idInt = id as int
		def accountIdInt = accountID as int
		def totalInt = total as int
		idInt + accountIdInt == totalInt
		
		where: 'value #accountID'
		[id, accountID, total] << provider.readCsv("test.csv")
	}
	
	@Unroll("Check post #json")
	def 'Check post'() {
		
		given: 'the inputs'
		def jsonSlurper = new JsonSlurper()
		def object = jsonSlurper.parseText(json)
		
		when: 'the conditionals'
		def response = restClient.post( path: 'SoftLayer_Virtual_Guest/createObject',
			requestContentType: ContentType.JSON,  body: object)

		then: 'the validations'
		response.status == 200

		and:
		response.data.startCpus == 1
		response.data.startCpus instanceof Integer
		
		where: 'the data providers'
		json | _
		'{"parameters": [{"hostname": "host1","domain": "example.com","startCpus": 1,"maxMemory": 1024,"hourlyBillingFlag": true,"localDiskFlag": true,"operatingSystemReferenceCode": "UBUNTU_LATEST","datacenter": {"name": "dal05"}}]}' | _
		'{"parameters": [{"hostname": "host2","domain": "example.com","startCpus": 1,"maxMemory": 1024,"hourlyBillingFlag": true,"localDiskFlag": true,"operatingSystemReferenceCode": "UBUNTU_LATEST","datacenter": {"name": "dal05"}}]}' | _
		'{"parameters": [{"hostname": "host3","domain": "example.com","startCpus": 1,"maxMemory": 1024,"hourlyBillingFlag": true,"localDiskFlag": true,"operatingSystemReferenceCode": "UBUNTU_LATEST","datacenter": {"name": "dal05"}}]}' | _
	}
	
	*/
}