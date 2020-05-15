/**
 *
 */
package Perf;

import java.util.ArrayList;

import BaseData.B;

/**
 * @author liwenjie
 *
 */
public class Data {
	 //part2������Ҫ
	//����ǲ������ģ�Ҫ���������ķ�����ʾ������Heavy API�ķ������ñ�ʾ��
    static String[] insert = {
    	"<android.database.sqlite.SQLiteDatabase: void beginTransaction()>",
    	"<android.database.sqlite.SQLiteDatabase: void endTransaction()>",
    	"<android.database.sqlite.SQLiteDatabase: long insert(java.lang.String,java.lang.String,android.content.ContentValues)>"};

    static String[] Bitmap={
    	"android.graphics.BitmapFactory",
    	"android.graphics.BitmapFactory.Options"};

    public static String[] Loop1={"<java.util.Set: int size()>",
		"<java.util.Map: int size()>",
		};
    public static String[] Loop2={
		"if",
		"goto"};


    static String[] a2 = { "db.beginTransaction()", "db.endTransaction()"};//�洢�̳еĳ�������
    static String[] b2 = { "android.view.View inflate(int,android.view.ViewGroup)" };//�洢��������
    static String[] c2 = { "android.view.View findViewById(int)" };//�洢������

    ArrayList<String> a=new ArrayList<String>();
    ArrayList<B> bb=new ArrayList<B>();
    ArrayList<String> e=new ArrayList<String>();
	//part1������Ҫ
	//ע�����������Ҫ�漰�����ࣺwidget(���ڲ���)��view(��ͼ)��------��û�ù�--�������"����$On��ͷ�ķ�����"
    void addData(){
    	//ȫ��Listener��---------��������ʲô���أ�
	a.add("android.widget.AbsListView$OnScrollListener");
    a.add("android.widget.AdapterView$OnItemClickListener");
    a.add("android.widget.AdapterView$OnItemLongClickListener");
    a.add("android.widget.AdapterView$OnItemSelectedListener");
    a.add("android.view.View$OnClickListener");
    a.add("android.view.View$OnLongClickListener");
    a.add("android.view.View$OnFocusChangeListener");
    a.add("android.view.View$OnKeyListener");
    a.add("android.view.View$OnTouchListener");
    a.add("android.view.View$OnCreateContextMenuListener");
    a.add("android.widget.CalendarView$OnDateChangeListener");
    a.add("android.widget.AutoCompleteTextView$OnDismissListener");
    a.add("android.widget.Chronometer$OnChronometerTickListener");
    a.add("android.widget.CompoundButton$OnCheckedChangeListener");
    a.add("android.widget.DatePicker$OnDateChangedListener");
    a.add("android.widget.ExpandableListView$OnChildClickListener");
    a.add("android.widget.ExpandableListView$OnGroupClickListener");
    a.add("android.widget.ExpandableListView$OnGroupCollapseListener");
    a.add("android.widget.ExpandableListView$OnGroupExpandListener");
    a.add("android.widget.NumberPicker$OnScrollListener");
    a.add("android.widget.NumberPicker$OnValueChangeListener");
    a.add("android.widget.PopupMenu$OnDismissListener");
    a.add("android.widget.PopupMenu$OnMenuItemClickListener");
    a.add("android.widget.PopupWindow$OnDismissListener");
    a.add("android.widget.RadioGroup$OnCheckedChangeListener");
    a.add("android.widget.RatingBar$OnRatingBarChangeListener");
    a.add("android.widget.SearchView$OnCloseListener");
    a.add("android.widget.SearchView$OnQueryTextListener");
    a.add("android.widget.SearchView$OnSuggestionListener");
    a.add("android.widget.SeekBar$OnSeekBarChangeListener");
    a.add("android.widget.ShareActionProvider$OnShareTargetSelectedListener");
    a.add("android.widget.SlidingDrawer$OnDrawerCloseListener");
    a.add("android.widget.SlidingDrawer$OnDrawerOpenListener");
    a.add("android.widget.SlidingDrawer$OnDrawerScrollListener");
    a.add("android.widget.TabHost$OnTabChangeListener");
    a.add("android.widget.TextView$OnEditorActionListener");
    a.add("android.widget.TimePicker$OnTimeChangedListener");
    a.add("android.widget.ZoomButtonsController$OnZoomListener");

    //part1��Ҫ������洢�Ŀ���������+����----����Ҫ��API
    //���磺     Method Name:  <android.support.v7.internal.widget.ActionBarOverlayLayout: void <clinit>()>
    bb.add(new B("java.net.URL", "<java.net.URL: java.net.URLConnection openConnection()>"));//����ĸ�ʽ��ǰ������ࣻ�������mehtod.ToString�������ʽ
    bb.add(new B("java.net.URL", "<java.net.URL: java.io.InputStream openStream()>"));
    bb.add(new B("java.net.URL", "<java.net.URL: java.lang.Object getContent()>"));
    bb.add(new B("java.net.URL", "<java.net.URL: java.lang.Object getContent(java.lang.Class[])>"));
    bb.add(new B("java.net.URLConnection", "<java.net.URLConnection: java.io.InputStream getInputStream()>"));
    bb.add(new B("java.net.URLConnection", "<java.net.URLConnection: java.io.OutputStream getOutputStream()>"));
    bb.add(new B("java.net.URLConnection", "<java.net.URLConnection: java.lang.Object getContent()>"));
    bb.add(new B("java.net.URLConnection", "<java.net.URLConnection: java.lang.Object getContent(java.lang.Class[])>"));
    bb.add(new B("java.net.URLConnection", "<java.net.URLConnection: java.lang.String getContentEncoding()>"));
    bb.add(new B("java.net.URLConnection", "<java.net.URLConnection: java.lang.String getContentType()>"));
    bb.add(new B("java.net.URLConnection", "<java.net.URLConnection: int getContentLength()>"));
    bb.add(new B("android.database.sqlite.SQLiteDatabase", "<android.database.sqlite.SQLiteDatabase: void execSQL(java.lang.String)>"));
    bb.add(new B("android.database.sqlite.SQLiteDatabase", "<android.database.sqlite.SQLiteDatabase: void execSQL(java.lang.String,java.lang.Object[])>"));
    bb.add(new B("android.database.sqlite.SQLiteDatabase", "<android.database.sqlite.SQLiteDatabase: android.database.Cursor query(java.lang.String,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String,java.lang.String,java.lang.String,java.lang.String)>"));
    bb.add(new B("android.database.sqlite.SQLiteDatabase", "<android.database.sqlite.SQLiteDatabase: android.database.Cursor query(boolean,java.lang.String,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String,java.lang.String,java.lang.String,java.lang.String,android.os.CancellationSignal)>"));
    bb.add(new B("android.database.sqlite.SQLiteDatabase", "<android.database.sqlite.SQLiteDatabase: android.database.Cursor query(java.lang.String,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String,java.lang.String,java.lang.String)>"));
    bb.add(new B("android.database.sqlite.SQLiteDatabase", "<android.database.sqlite.SQLiteDatabase: android.database.Cursor query(boolean,java.lang.String,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String,java.lang.String,java.lang.String,java.lang.String)>"));
    bb.add(new B("android.database.sqlite.SQLiteDatabase", "<android.database.sqlite.SQLiteDatabase: android.database.Cursor queryWithFactory(android.database.sqlite.SQLiteDatabase$CursorFactory,boolean,java.lang.String,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String,java.lang.String,java.lang.String,java.lang.String,android.os.CancellationSignal)>"));
    bb.add(new B("android.database.sqlite.SQLiteDatabase", "<android.database.sqlite.SQLiteDatabase: android.database.Cursor queryWithFactory(android.database.sqlite.SQLiteDatabase$CursorFactory,boolean,java.lang.String,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String,java.lang.String,java.lang.String,java.lang.String)>"));
    bb.add(new B("android.database.sqlite.SQLiteDatabase", "<android.database.sqlite.SQLiteDatabase: android.database.Cursor rawQuery(java.lang.String,java.lang.String[],android.os.CancellationSignal)>"));
    bb.add(new B("android.database.sqlite.SQLiteDatabase", "<android.database.sqlite.SQLiteDatabase: android.database.Cursor rawQuery(java.lang.String,java.lang.String[])>"));
    bb.add(new B("android.database.sqlite.SQLiteDatabase", "<android.database.sqlite.SQLiteDatabase: android.database.Cursor rawQueryWithFactory(android.database.sqlite.SQLiteDatabase$CursorFactory,java.lang.String,java.lang.String[],java.lang.String)>"));
    bb.add(new B("android.database.sqlite.SQLiteDatabase", "<android.database.sqlite.SQLiteDatabase: android.database.Cursor rawQueryWithFactory(android.database.sqlite.SQLiteDatabase$CursorFactory,java.lang.String,java.lang.String[],java.lang.String,android.os.CancellationSignal)>"));
    bb.add(new B("android.context.ContextWrapper", "<android.context.ContextWrapper: java.io.FileInputStream openFileInput(java.lang.String)>"));
    bb.add(new B("android.context.ContextWrapper", "<android.context.ContextWrapper: java.io.FileOutputStream openFileOutput(java.lang.String,int)>"));
    bb.add(new B("java.io.Reader", "<java.io.Reader: int read()>"));
    bb.add(new B("java.io.Reader", "<java.io.Reader: int read(char[])>"));
    bb.add(new B("java.io.Reader", "<java.io.Reader: int read(java.nio.CharBuffer)>"));
    bb.add(new B("java.io.InputStreamReader", "<java.io.InputStreamReader: int read()>"));
    bb.add(new B("java.io.InputStreamReader", "<java.io.InputStreamReader: int read(char[],int,int)>"));
    bb.add(new B("java.io.BufferedReader", "<java.io.BufferedReader: int read()>"));
    bb.add(new B("java.io.BufferedReader", "<java.io.BufferedReader: int read(char[],int,int)>"));
    bb.add(new B("java.io.BufferedReader", "<java.io.BufferedReader: java.lang.String readLine()>"));
    bb.add(new B("java.io.Writer", "<java.io.Writer: java.io.Writer append(char)>"));
    bb.add(new B("java.io.Writer", "<java.io.Writer: java.io.Writer append(java.lang.CharSequence)>"));
    bb.add(new B("java.io.Writer", "<java.io.Writer: java.io.Writer append(java.lang.CharSequence,int,int)>"));
    bb.add(new B("java.io.Writer", "<java.io.Writer: void write(char[])>"));
    bb.add(new B("java.io.Writer", "<java.io.Writer: void write(int)>"));
    bb.add(new B("java.io.Writer", "<java.io.Writer: void write(java.lang.String)>"));
    bb.add(new B("java.io.Writer", "<java.io.Writer: void write(java.lang.String,int,int)>"));
    bb.add(new B("java.io.BufferedWriter", "<java.io.BufferedWriter: void write(char[],int,int)>"));
    bb.add(new B("java.io.BufferedWriter", "<java.io.BufferedWriter: void write(int)>"));
    bb.add(new B("java.io.BufferedWriter", "<java.io.BufferedWriter: void write(java.lang.String,int,int)>"));
    bb.add(new B("android.graphics.BitmapFactory", "<android.graphics.BitmapFactory: android.graphics.Bitmap decodeFile(java.lang.String,android.graphics.BitmapFactory$Options)>"));
    bb.add(new B("android.graphics.Bitmap", "<android.graphics.Bitmap: boolean compress(android.graphics.Bitmap$CompressFormat,int,java.io.OutputStream)>"));


    //�����쳣�࣬����û���õ���
    e.add("java.lang.ArithmeticException");
    e.add("java.lang.ArrayStoreException");
    e.add("java.lang.ClassCastException");
    e.add("java.lang.IllegalMonitorStateException");
    e.add("java.lang.IndexOutOfBoundsException");
    e.add("java.lang.ArrayIndexOutOfBoundsException");
    e.add("java.lang.NegativeArraySizeException");
    e.add("java.lang.NullPointerException");
    e.add("java.lang.InstantiationError");
    e.add("java.lang.InternalError");
    e.add("java.lang.OutOfMemoryError");
    e.add("java.lang.StackOverflowError");
    e.add("java.lang.UnknownError");
    e.add("java.lang.ThreadDeath");
    e.add("java.lang.ClassCircularityError");
    e.add("java.lang.IllegalAccessError");
    e.add("java.lang.NoClassDefFoundError");
    e.add("java.lang.VerifyError");
    e.add("java.lang.NoSuchFieldError");
    e.add("java.lang.AbstractMethodError");
    e.add("java.lang.NoSuchMethodError");
    e.add("java.lang.UnsatisfiedLinkError");
    }

}
