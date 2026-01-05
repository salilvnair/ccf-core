package com.github.salilvnair.ccf.util.log;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import com.github.salilvnair.ccf.util.commonutil.file.FilenameUtils;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StackTraceFrame {
	
  private String className;
  private String methodName;
  private String fileName;
  private int lineNumber;
  private boolean isNative;
  private String errorStack;
  private Object uniqueId;
  private String uniqueIdName;
  private final String errorString;
  private List<String> printPackages;
  private List<String> ignorePackages;
  protected final Logger logger;
  private final StackTraceElement[] stackTrace;
  private Class<?> currentClass = StackTraceFrame.class;
  public static final String ANY_PACKAGE="*";
  public StackTraceFrame(Exception ex) {
	super();
	logger = LoggerFactory.getLogger(getClass());
	this.errorString = ""+getRootCause(ex);
	this.stackTrace = ex.getStackTrace();
	if(stackTrace!=null && stackTrace.length>0) {
	  this.className =  stackTrace[0].getClassName();
	  this.methodName = stackTrace[0].getMethodName();
	  this.fileName = stackTrace[0].getFileName();
	  this.lineNumber = stackTrace[0].getLineNumber();
	  this.isNative = stackTrace[0].isNativeMethod();
	}
  }

	public StackTraceFrame(InvocationTargetException ex, Class<?> currentClass) {
		super();
		this.currentClass = currentClass;
		logger = LoggerFactory.getLogger(currentClass);
		this.errorString = ""+getRootCause(ex.getTargetException());
		this.stackTrace = ex.getTargetException().getStackTrace();
		if(stackTrace!=null && stackTrace.length>0) {
			this.className =  stackTrace[0].getClassName();
			this.methodName = stackTrace[0].getMethodName();
			this.fileName = stackTrace[0].getFileName();
			this.lineNumber = stackTrace[0].getLineNumber();
			this.isNative = stackTrace[0].isNativeMethod();
		}
	}

	public StackTraceFrame(Throwable throwable, Class<?> currentClass) {
		super();
		this.currentClass = currentClass;
		logger = LoggerFactory.getLogger(currentClass);
		this.errorString = ""+getRootCause(throwable);
		this.stackTrace = throwable.getStackTrace();
		if(stackTrace!=null && stackTrace.length>0) {
			this.className =  stackTrace[0].getClassName();
			this.methodName = stackTrace[0].getMethodName();
			this.fileName = stackTrace[0].getFileName();
			this.lineNumber = stackTrace[0].getLineNumber();
			this.isNative = stackTrace[0].isNativeMethod();
		}
	}
  
  public StackTraceFrame(Exception ex, Class<?> currentClass) {
	super();
	this.currentClass = currentClass;
	logger = LoggerFactory.getLogger(currentClass);
	this.errorString =  ""+getRootCause(ex);
	this.stackTrace = ex.getStackTrace();
  }

  @Override
  public String toString() {	  
	  if(uniqueIdName!=null && uniqueId!=null) {
		  if(this.errorStack!=null) {
			  errorStack =  "StackTraceFrames:<"+uniqueIdName+":"+uniqueId+">\n\t" + this.errorStack;
		  }
		  else{
			  errorStack = "StackTraceFrames:<"+uniqueIdName+":"+uniqueId+"> [className=" + className + ", methodName=" + methodName + ", fileName=" + fileName
					  + ", lineNumber=" + lineNumber + ", isNative=" + isNative + "]";
		  }
					
	  }
	  else if(uniqueId!=null) {
		  if(this.errorStack!=null) {
			  errorStack =  "StackTraceFrames:<"+uniqueId+">\n\t" + this.errorStack;
		  }
		  else{
			  errorStack = "StackTraceFrames:<"+uniqueId+"> [className=" + className + ", methodName=" + methodName + ", fileName=" + fileName
						+ ", lineNumber=" + lineNumber + ", isNative=" + isNative + "]";
		  }		  
	  }
	  else {
		  if(this.errorStack!=null) {
			  errorStack = "StackTraceFrames:\n\t" + this.errorStack;
		  }
		  else {
			  errorStack = "StackTraceFrames: [className=" + className + ", methodName=" + methodName + ", fileName=" + fileName
						+ ", lineNumber=" + lineNumber + ", isNative=" + isNative + "]";
		  }
	  }

	  return errorStack;
  }
  
  public Throwable getRootCause(Throwable ex) {
	    Throwable cause = null; 
	    Throwable result = ex;

	    while(null != (cause = result.getCause())  && (result != cause) ) {
	        result = cause;
	    }
	    return result;
  }
    
  public void printStackTrace() {
	  logger.error(errorStackTrace());
  }

  public String errorStackTrace() {
	  if(stackTrace!=null && stackTrace.length>0) {
		  StringBuilder errorStackBuilder = new StringBuilder();
		  String aboveOneLevelStack = "";
		  for(StackTraceElement stackTraceElement : stackTrace) {
			  this.className =  stackTraceElement.getClassName();
			  this.methodName = stackTraceElement.getMethodName();
			  this.fileName = stackTraceElement.getFileName();
			  this.lineNumber = stackTraceElement.getLineNumber();
			  this.isNative = stackTraceElement.isNativeMethod();
			  String simpleClassName = FilenameUtils.removeExtension(this.fileName);
			  if(!classInPackage(this.className)) {
				  aboveOneLevelStack =  "[" + simpleClassName + "](" + methodName + "){#" + lineNumber + "}\n\t";
			  }
			  else {
				  errorStackBuilder.append("[").append(simpleClassName).append("](").append(methodName).append("){#").append(lineNumber).append("}");
				  errorStackBuilder.append("\n\t");
			  }
			  if(currentClass.getName().equals(stackTraceElement.getClassName())) {
				  break;
			  }
		  }
		  if(!errorStackBuilder.toString().isEmpty()) {
			  errorStack = aboveOneLevelStack + errorStackBuilder;
		  }
	  }
	  return errorString+":"+this;
  }

  public List<String> allPrintPackages() {
	if(printPackages==null) {
		printPackages = new ArrayList<>();

	}
	return printPackages;
  }

  
  public void addPrintPackage(String printPackage) {
	  if(printPackages==null) {
		printPackages = new ArrayList<>();		
		printPackages.add(printPackage);
	  }
      else {
          printPackages.add(printPackage);
      }
  }

	public void addIgnorePackage(String ignorePackage) {
		if(ignorePackages==null) {
			ignorePackages = new ArrayList<>();
			ignorePackages.add(ignorePackage);
		}
		else {
			ignorePackages.add(ignorePackage);
		}
	}
  
  public boolean classInPackage(String className) {
      boolean printAnyPackage = allPrintPackages().stream().anyMatch(ANY_PACKAGE::equals);
      if(printAnyPackage) {
          return true;
      }
	  if(!CollectionUtils.isEmpty(ignorePackages)) {
		  boolean packageExclusionResult = ignorePackages.stream().anyMatch(className::contains);
		  if(packageExclusionResult) {
			  return false;
		  }
	  }
	  for(String packageName: allPrintPackages()) {
		  if(className.contains(packageName)) {
			  return true;
		  }
	  }
	  return false;
  }

  public static Logger initLogger(Class<?> clazz) {
	  return LoggerFactory.getLogger(clazz);
  }
}
