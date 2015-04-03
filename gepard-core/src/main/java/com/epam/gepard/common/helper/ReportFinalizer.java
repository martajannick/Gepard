package com.epam.gepard.common.helper;

/*==========================================================================
 Copyright 2004-2015 EPAM Systems

 This file is part of Gepard.

 Gepard is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Gepard is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Gepard.  If not, see <http://www.gnu.org/licenses/>.
===========================================================================*/

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Properties;

import com.epam.gepard.common.Environment;
import com.epam.gepard.common.GepardConstants;
import com.epam.gepard.generic.GenericListTestSuite;
import com.epam.gepard.helper.AllTestResults;

/**
 * Finalizes the information holder with the specific datas.
 * @author Zsolt Kiss Gere, Laszlo Toth, Tamas Godan, Tamas Kohegyi, Tibor Kovacs
 */
public class ReportFinalizer {

    /**
     * Sets all necessary information into the information holder object.
     * @param gSuite is the current test suite.
     * @param allTestResults is the holder of test results.
     * @param applicationUnderTestVersion is the current application version.
     * @param elapsedTime is the difference between start and end.
     * @param props is the information holder.
     */
    public void finalizeTheReport(final GenericListTestSuite gSuite, final AllTestResults allTestResults, final String applicationUnderTestVersion,
            final long elapsedTime, final Properties props) {
        Calendar cal;
        Environment.getScript().updateStatus();
        cal = Calendar.getInstance();
        double duration = elapsedTime / GepardConstants.ONE_SECOND_LENGTH.getConstant();
        int minuteDuration = (int) Math.floor(duration / GepardConstants.ONE_MINUTE_IN_SECS.getConstant());
        double secondDuration = duration - (minuteDuration * GepardConstants.ONE_MINUTE_IN_SECS.getConstant());
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(0);
        nf.setMaximumFractionDigits(0);
        props.setProperty("Runned", String.valueOf(allTestResults.getRunned()));
        props.setProperty("Failed", String.valueOf(allTestResults.getFailed()));
        props.setProperty("Passed", String.valueOf(allTestResults.getPassed()));
        props.setProperty("TCdummy", String.valueOf(allTestResults.getDummy()));
        props.setProperty("TCnotapplicable", String.valueOf(allTestResults.getNotApplicable()));
        props.setProperty("Time", "<b>" + minuteDuration + "</b> minutes and <b>" + nf.format(secondDuration) + "</b> seconds");
        props.setProperty("SecondsTime", "" + duration);
        props.setProperty("DateTime", GenericListTestSuite.formatDateTime(cal));
        String applicationVersion = applicationUnderTestVersion;
        if (applicationVersion == null) {
            applicationVersion = "undetected";
        }
        props.setProperty("Version", applicationVersion);
        String tsid = Environment.getProperty(Environment.TEST_SYSTEM_ID, "Unknown");
        props.setProperty("TSID", tsid);
        props.setProperty("TCSrunned", String.valueOf(GenericListTestSuite.getTestClassCount()));
        props.setProperty("TCUsed", Integer.toString(gSuite.getUsedTc()));
        props.setProperty("TCNumber", Integer.toString(gSuite.getNumberTc()));
        String resultUrl = Environment.getProperty(Environment.GEPARD_PUBLIC_PATH) + "/" + Environment.getProperty(Environment.GEPARD_PUBLIC_RESULT);
        String resultUrlHtml = "Test Results will be available <a href=" + resultUrl + ">here.</a><br/>";
        if (Environment.getBooleanProperty(Environment.GEPARD_PUBLIC_ENABLED)) {
            props.setProperty("ResultURLHTML", resultUrlHtml);
        } else {
            props.setProperty("ResultURLHTML", "");
        }
    }
}
