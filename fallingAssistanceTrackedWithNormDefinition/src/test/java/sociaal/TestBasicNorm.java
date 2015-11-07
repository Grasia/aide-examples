package sociaal;

import static org.junit.Assert.*;
import jade.core.AID;
import jade.domain.introspection.Event;
import jade.lang.acl.ACLMessage;
import jade.wrapper.StaleProxyException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.cdi.KSession;
import org.kie.api.event.process.ProcessEventListener;
import org.kie.api.event.process.ProcessNodeLeftEvent;
import org.kie.api.event.process.ProcessNodeTriggeredEvent;
import org.kie.api.event.process.ProcessStartedEvent;
import org.kie.api.event.process.ProcessVariableChangedEvent;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.event.rule.AgendaGroupPoppedEvent;
import org.kie.api.event.rule.AgendaGroupPushedEvent;
import org.kie.api.event.rule.BeforeMatchFiredEvent;
import org.kie.api.event.rule.DebugAgendaEventListener;
import org.kie.api.event.rule.DebugRuleRuntimeEventListener;
import org.kie.api.event.rule.MatchCancelledEvent;
import org.kie.api.event.rule.MatchCreatedEvent;
import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.kie.api.event.rule.RuleFlowGroupActivatedEvent;
import org.kie.api.event.rule.RuleFlowGroupDeactivatedEvent;
import org.kie.api.event.rule.RuleRuntimeEventListener;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.testng.Assert;

import com.jme3.math.Vector3f;

import phat.agents.AgentPHATEvent;
import phat.body.BodyUtils.BodyPosture;
import phat.world.MonitorEventQueue;
import phat.world.MonitorEventQueueImp;
import phat.world.PHATCalendar;
import phat.world.RemotePHATEvent;
import sociaal.norms.AssistWhilePatientDrinks;
import sociaal.ontology.Action;
import sociaal.ontology.ActionPerformed;
import sociaal.ontology.Assisting;
import sociaal.ontology.CurrentTime;
import sociaal.ontology.GoingToFail;
import sociaal.ontology.OnTheGround;
import sociaal.ontology.Partner;

public class TestBasicNorm {

	static MonitorEventQueueImp meq=new MonitorEventQueueImp();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		meq.startServer(MonitorEventQueue.DefaultName);
	}

	public void pause(long millis){
		try {
			Thread.currentThread().sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void checkInstancesCount(KieSession ksession, Class controlledClass, int expectedInstancesCount) {
		Assert.assertTrue(countInstanceTypes(ksession, controlledClass)==expectedInstancesCount, 
				"There should be "+expectedInstancesCount+" "+controlledClass.getName()+" instance and there are "+countInstanceTypes(ksession, controlledClass)+"."+
						"Working memory:\n"+getWorkingMemory(ksession));
	}
	
	
	@Test
	public void testRuleTimeout() throws StaleProxyException, IOException{
		System.err.println(new Date().getTime());
		System.err.println(new PHATCalendar().getTimeInMillis());
		KieSession ksession = getSession("src/test/resources/basicpattern.drl");
		AgentPHATEvent ape=new AgentPHATEvent("E3Patient",new Vector3f(0f,0f,0f),
				new PHATCalendar(),BodyPosture.Standing,"walking"+".");
		ape.setActionType("Drink");
		ksession.insert(ape);
		ksession.fireAllRules();
		checkInstancesCount(ksession,Partner.class,2);
		checkInstancesCount(ksession,ActionPerformed.class,1);
		checkInstancesCount(ksession,AssistWhilePatientDrinks.class,0);
		pause(1000);
		AgentPHATEvent anotherEvent=
				new AgentPHATEvent("E3Husband",
						new Vector3f(0f,0f,0f),
				new PHATCalendar(),
				BodyPosture.Standing,"walking"+".");
		anotherEvent.setActionType("Drink");
		ksession.insert(anotherEvent);
		ksession.fireAllRules();
		checkInstancesCount(ksession,AssistWhilePatientDrinks.class,0);
		
		pause(2000);
		AgentPHATEvent anotherEvent1=
				new AgentPHATEvent("E3Husband",
						new Vector3f(0f,0f,0f),
				new PHATCalendar(),
				BodyPosture.Standing,"walking"+".");
		anotherEvent1.setActionType("Drink1");
		ksession.insert(anotherEvent1);
		ksession.fireAllRules();
		checkInstancesCount(ksession,AssistWhilePatientDrinks.class,1);
		
		AgentPHATEvent anotherEvent2=
				new AgentPHATEvent("E3Husband",
						new Vector3f(0f,0f,0f),
				new PHATCalendar(),
				BodyPosture.Standing,"walking"+".");
		anotherEvent2.setActionType("Drink1");
		ksession.insert(anotherEvent2);
		ksession.fireAllRules();
		checkInstancesCount(ksession,AssistWhilePatientDrinks.class,1);
		
		
	}


	private int countInstanceTypes(KieSession ksession, Class instanceClass){
		int count=0;
		for (Object obj:ksession.getObjects())
			if (obj.getClass().equals(instanceClass))
				count++;
		return count;
	}

	private static void printWorkingMemory(KieSession ksession) {
		System.out.println("--------BEGIN Working Memory-------------");
		for (Object obj:ksession.getObjects())
			System.out.println(obj);
		System.out.println("--------END Working Memory-------------");
	}

	private static String getWorkingMemory(KieSession ksession) {
		String result=("--------BEGIN Working Memory-------------\n");
		for (Object obj:ksession.getObjects())
			result=result+obj.toString()+"\n";
		result=result+("--------END Working Memory-------------\n");
		return result;
	}

	private KieSession getSession(String ruleFile){
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		
		KieServices kServices = KieServices.Factory.get();
		FileInputStream fis=null;
		try {
			fis = new FileInputStream(ruleFile);
			kbuilder.add(ResourceFactory.newInputStreamResource(fis), ResourceType.DRL);
			System.out.println( "Loading file: " + ruleFile );
			
			return kbuilder.newKnowledgeBase().newKieSession();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}



	public static void main(String[] args){
		System.out.println(Calendar.getInstance().getTimeInMillis());
	}

}
