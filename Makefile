CC = javac
BIN = bin
SRC = src
LIB = "lib/*"
CLASSES = $(SRC)/Envelope.java $(SRC)/Email.java $(SRC)/MessageTableSelectionListener.java $(SRC)/ComposeMailListener.java $(SRC)/StatusPanel.java $(SRC)/EmailMessage.java $(SRC)/ComposeMailDialog.java $(SRC)/RemoveFileListener.java $(SRC)/JMimePane.java $(SRC)/MessageTableModel.java $(SRC)/UnrecognisedEmailServiceException.java $(SRC)/DownloadAttachmentJButton.java $(SRC)/RefreshListener.java $(SRC)/ProtocolType.java

Envelope : 
	$(CC) -sourcepath $(SRC) -cp $(LIB) $(CLASSES) -d $(BIN)

clean :
	rm -f -r $(BIN)/* 

run :
	cd $(BIN); java -cp ../$(LIB):. envelope.Envelope

debug :
	cd $(BIN); jdb -classpath ../$(LIB):. -sourcepath ../$(SRC)/*:. envelope.Envelope
