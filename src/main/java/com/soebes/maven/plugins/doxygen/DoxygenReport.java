/**
 * The Doxygen Maven Plugin (dmp)
 *
 * Copyright (c) 2010, 2011, 2012, 2013, 2014, 2015 by SoftwareEntwicklung Beratung Schulung (SoEBeS)
 * Copyright (c) 2010, 2011, 2012, 2013, 2014, 2015 by Karl Heinz Marbaise
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.soebes.maven.plugins.doxygen;

import org.apache.maven.doxia.siterenderer.RenderingContext;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.doxia.siterenderer.sink.SiteRendererSink;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.MavenReport;
import org.apache.maven.reporting.MavenReportException;

import java.io.File;
import java.util.Locale;


/**
 * This part will create the report.
 * 
 */
@Mojo( name = "report", defaultPhase = LifecyclePhase.SITE, requiresProject = true, threadSafe = true )
public class DoxygenReport 
    extends AbstractDoxygenMojo
    implements MavenReport
{

    @Component
    private Renderer siteRenderer;
	
    /**
	 * Specifies the destination directory where javadoc saves the generated HTML files.
	 *
	 */
    @Parameter(defaultValue = "${project.reporting.outputDirectory}", property = "reportOutputDirectory", required = true)
	private File reportOutputDirectory;
	
    /**
     * The name of the destination directory.
     *
     */
    @Parameter(defaultValue = "doxygen", property = "destDir")
    private String destDir;

    
    /** {@inheritDoc} */
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        if ( isSkip() )
        {
            getLog().info( "Skipping doxgenc generation" );
            return;
        }

        try
        {
            RenderingContext context = new RenderingContext( getOutputDirectory(), getOutputName() + ".html" );
            SiteRendererSink sink = new SiteRendererSink( context );
            Locale locale = Locale.getDefault();
            generate( sink, locale );
        }
        catch ( MavenReportException e )
        {
//            if ( failOnError )
//            {
//                throw new MojoExecutionException( "An error has occurred in " + getName( Locale.ENGLISH )
//                    + " report generation:" + e.getMessage(), e );
//            }

            getLog().error( "An error has occurred in " + getName( Locale.ENGLISH )
                    + " report generation:" + e.getMessage(), e );
        }
        catch ( RuntimeException e )
        {
            getLog().error( e.getMessage(), e );
        }
    }
    
    /** {@inheritDoc} */
    public void generate(Sink sink, Locale locale )
        throws MavenReportException
    {
        setOutputDirectory(getReportOutputDirectory());

        executeReport( locale );
    }


    /** {@inheritDoc} */
	public File getReportOutputDirectory() {
		if (reportOutputDirectory == null) {
			return reportOutputDirectory;
		}

		return reportOutputDirectory;
	}
	

	protected MavenProject getProject() {
		return getProject();
	}

    /** {@inheritDoc} */
	public String getCategoryName()
    {
        return CATEGORY_PROJECT_REPORTS;
    }


    /** {@inheritDoc} */
    public String getOutputName()
    {
        return "doxygen" + "/index";
    }

    /** {@inheritDoc} */
    public boolean isExternalReport()
    {
        return true;
    }

    /** {@inheritDoc} */
	public String getDescription(Locale locale) {
		return "Doxygen Report";
	}

    /** {@inheritDoc} */
	public String getName(Locale locale) {
		return "Doxygen";
	}

	public void setDestDir(String destDir) {
		this.destDir = destDir;
	}

	public String getDestDir() {
		return destDir;
	}

	public boolean canGenerateReport() {
		return true;
	}

	public void setReportOutputDirectory(File reportOutputDirectory) {
		this.reportOutputDirectory = getOutputDirectory();
	}
}
