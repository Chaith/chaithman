package com.scholastic.sso;

import java.util.List;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;
import org.testng.internal.Utils;
import org.testng.internal.Utils;

import com.scholastic.sso.TestBase;
	
	public class CustomListener extends TestListenerAdapter implements IInvokedMethodListener{

		public void onTestFailure(ITestResult tr){

		TestBase.APPLICATION_LOG.debug("Fail - "+ tr.getName());
		}

		public void onTestSkipped(ITestResult tr){

		TestBase.APPLICATION_LOG.debug("Skipped - "+ tr.getName());
		}

		public void onTestSuccess(ITestResult tr){

		TestBase.APPLICATION_LOG.debug("Successs- "+ tr.getName());
		}

		public void afterInvocation(IInvokedMethod method, ITestResult result){
		}

		public void beforeInvocation(IInvokedMethod arg0, ITestResult test){
		}

}


