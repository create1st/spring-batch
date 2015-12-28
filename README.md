# Spring-Boot + Spring-Batch + Spring Integration + ActiveMQ + Spring-Data + H2

This is simply DEMO app to demonstrate Spring-Batch processing. It is monitoring C:\Temp directory for *.CVS files containing "Ticket" data and start processing when new file is detected.

##Ticket file format
'''
Ticket_0,20-12-2015,Test ticket,INTERNAL
Ticket_1,20-12-2015,Test ticket,EXTERNAL
'''

##Remarks
- The date in ticket file has to match current date. Otherwise the line is considered as obsolete
- While tickets are processed several metrics is being evaluated
- After successful import of the ticket it is being persisted into the H2 database and then sent to JMS queue
- JMS listener does nothing at all with the incoming tickets - just prints the event into the log. This is just to "simulate" tickets collected by external system.


