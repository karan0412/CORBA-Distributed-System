package PeerApp;


/**
* PeerApp/RegisterPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from PeerApp.idl
* Sunday, 29 October 2023 10:39:53 AM FJT
*/

public abstract class RegisterPOA extends org.omg.PortableServer.Servant
 implements PeerApp.RegisterOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("connect", new java.lang.Integer (0));
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
       case 0:  // PeerApp/Register/connect
       {
         String name = in.read_string ();
         int $result = (int)0;
         $result = this.connect (name);
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
    "IDL:PeerApp/Register:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public Register _this() 
  {
    return RegisterHelper.narrow(
    super._this_object());
  }

  public Register _this(org.omg.CORBA.ORB orb) 
  {
    return RegisterHelper.narrow(
    super._this_object(orb));
  }


} // class RegisterPOA
