package PeerApp;


/**
* PeerApp/PeerPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from PeerApp.idl
* Sunday, 29 October 2023 10:39:53 AM FJT
*/

public abstract class PeerPOA extends org.omg.PortableServer.Servant
 implements PeerApp.PeerOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("setup", new java.lang.Integer (0));
    _methods.put ("startElection", new java.lang.Integer (1));
    _methods.put ("message1", new java.lang.Integer (2));
    _methods.put ("message2", new java.lang.Integer (3));
    _methods.put ("announceLeader", new java.lang.Integer (4));
    _methods.put ("passCoordinator", new java.lang.Integer (5));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // PeerApp/Peer/setup
       {
         int max = in.read_long ();
         String nextName = in.read_string ();
         int $result = (int)0;
         $result = this.setup (max, nextName);
         out = $rh.createReply();
         out.write_long ($result);
         break;
       }

       case 1:  // PeerApp/Peer/startElection
       {
         int $result = (int)0;
         $result = this.startElection ();
         out = $rh.createReply();
         out.write_long ($result);
         break;
       }

       case 2:  // PeerApp/Peer/message1
       {
         int receivedMax = in.read_long ();
         int $result = (int)0;
         $result = this.message1 (receivedMax);
         out = $rh.createReply();
         out.write_long ($result);
         break;
       }

       case 3:  // PeerApp/Peer/message2
       {
         int receivedLeft = in.read_long ();
         int $result = (int)0;
         $result = this.message2 (receivedLeft);
         out = $rh.createReply();
         out.write_long ($result);
         break;
       }

       case 4:  // PeerApp/Peer/announceLeader
       {
         String name = in.read_string ();
         int max = in.read_long ();
         int $result = (int)0;
         $result = this.announceLeader (name, max);
         out = $rh.createReply();
         out.write_long ($result);
         break;
       }

       case 5:  // PeerApp/Peer/passCoordinator
       {
         int $result = (int)0;
         $result = this.passCoordinator ();
         out = $rh.createReply();
         out.write_long ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:PeerApp/Peer:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public Peer _this() 
  {
    return PeerHelper.narrow(
    super._this_object());
  }

  public Peer _this(org.omg.CORBA.ORB orb) 
  {
    return PeerHelper.narrow(
    super._this_object(orb));
  }


} // class PeerPOA
