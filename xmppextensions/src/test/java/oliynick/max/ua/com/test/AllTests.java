package oliynick.max.ua.com.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * <p>Runs all IQ tests</p>
 * <p>Created 02.03.16</p>
 * @author Max Oliynick
 * */

@RunWith(Suite.class)
@SuiteClasses({ TestChatListIQ.class, TestChatListRetrieveIQ.class, TestArchiveManager.class })
public final class AllTests {}
