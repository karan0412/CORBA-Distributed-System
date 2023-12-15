package PeerApp;


/**
* PeerApp/RegisterHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from PeerApp.idl
* Sunday, 29 October 2023 10:39:53 AM FJT
*/

abstract public class RegisterHelper
{
  private static String  _id = "IDL:PeerApp/Register:1.0";

  public static void insert (org.omg.CORBA.Any a, PeerApp.Register that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static PeerApp.Register extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (PeerApp.RegisterHelper.id (), "Register");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static PeerApp.Register read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_RegisterStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, PeerApp.Register value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static PeerApp.Register narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof PeerApp.Register)
      return (PeerApp.Register)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      PeerApp._RegisterStub stub = new PeerApp._RegisterStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static PeerApp.Register unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof PeerApp.Register)
      return (PeerApp.Register)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      PeerApp._RegisterStub stub = new PeerApp._RegisterStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}
