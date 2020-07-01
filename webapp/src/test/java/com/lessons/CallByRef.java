package com.lessons;

import com.lessons.services.ReportsService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class CallByRef
{
    private static final Logger logger = LoggerFactory.getLogger(ReportsService.class);

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
//        logger.debug("logger for test ");
//        assertTrue( true );
//
//
//        int i = 7;
//        changeMe(i);
//        logger.debug("i = {}", i);
//
//        i = changeMe2(i);
//        logger.debug("i = {}", i);
//
//        Person p = new Person();
//        p.setName("Adam");
//        p = changeMe4(p);
//        logger.debug("p.name = {}",p.getName());
//
//        String s = "Hi";
//        changeMe5(s);
//        logger.debug("After s = {}", s);

//        Report r = new Report();
//        r.setDisplayName("stuff");
//        changeMe6(r);
    }


//1 this method does nothing, was an example - 2 DO NOT DO THIS - DO NOT CHANGE THE PASSED IN VALUE
//    private void changeMe6(Report aReport)
//    {
//        aReport = new Report();
//        aReport.setDisplayName("rpt1.txt");
//    }

//    private void changeMe5(String aValue)
//    {
//        //this won't actually change anything - creates a new id with a string value, but the original string value remains/overrules
//        aValue = "Mom";
//    }
//
//    private Person changeMe4(Person aPerson)
//    {
//        //makes a copy of what I already have, reading from already existing
//        Person p = new Person();
//        p.setName(aPerson.getName());
//        p.setId(aPerson.getId());
//        p.setGrade(aPerson.getGrade());
//
//        //sets grade to 4
//        p.setGrade(4);
//        return p;
//    }
//
//    private void changeMe3(Person aPerson)
//    {
//        aPerson.setName("john smith");
//    }
//
//    private int changeMe2(int aValue)
//    {
//        int value = aValue + 10;
//        return value;
//    }
//
//
//    private void changeMe(int aValue)
//    {
//      aValue = 1000;
//      logger.debug("changeMe");
//
//    }

    @Test
    public void fixedListTest() {
        logger.debug("starting fixed list test");
        List<Integer> myFixedList= Arrays.asList(27, 22, 23);
        myFixedList.add(24);

        List<Integer> newList = new ArrayList<>(myFixedList);
    }

    @Test
    public void fixedListTest2() {
        logger.debug("starting fixed list test 2");
        List<String> noiseWords = Arrays.asList("the", "is", "of", "for", "a");

        logger.debug("Before going through loop:  noiseWords.size={}", noiseWords.size() );

        List<String> realWords = new ArrayList<>();
        realWords.add("hi");
        realWords.add("the");
        realWords.add("is");
        realWords.add("of");

        logger.debug("Before going through loop:  realWords.size={}", realWords.size() );

        Iterator<String> iter = realWords.iterator();
        while (iter.hasNext())
        {
            String sItem = iter.next();
//list always comes after :
            for (String noiseWord : noiseWords){

                if (sItem.equals(noiseWord))
                {
                    // remove the item from the list
                    iter.remove();
                }
            }
        }
        //this does the same as the above code
        //realWords.removeAll(noiseWords);
// The list size should now be 2
        logger.debug("After going through loop:  myList.size={}", realWords.size() );
    }
}
