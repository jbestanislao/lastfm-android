package fm.last.tag;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import fm.last.EasyElement;
import fm.last.ws.RequestManager;
import fm.last.ws.RequestParameters;
import fm.last.ws.Response;
import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TagAdapter extends BaseAdapter
{
	private ArrayList< String > m_tagNames = new ArrayList< String >();
	private Activity m_activity;
	
	public TagAdapter( Activity activity )
	{
		m_activity = activity;
	}
	
	public void getTopTags()
	{
		RequestManager.version2().callMethod( "tag.getTopTags", new RequestParameters(), m_topTagsEventHandler );
	}
	
	public void getSimilarTags( String tag )
	{
		RequestParameters params = new RequestParameters();
		params.add( "tag", tag);
		RequestManager.version2().callMethod( "tag.getSimilar", params, m_similarTagsEventHandler );
	}
	
	public void removeItem( int position )
	{
		m_tagNames.remove( position );
		notifyDataSetChanged();
	}
	
	public int getCount()
	{
		return m_tagNames.size();
	}

	public Object getItem( int position )
	{
		return m_tagNames.get( position );
	}

	public long getItemId( int position )
	{
		// TODO Auto-generated method stub
		return position;
	}

	public View getView( int position, View convertView, ViewGroup parent )
	{
		if( convertView == null )
		{
			convertView = new LinearLayout( m_activity );
			TextView tv = new TextView( m_activity );
			tv.setGravity( Gravity.CENTER_VERTICAL );
			tv.setPadding( 4, 0, 4, 0 );
			tv.setLayoutParams( new LinearLayout.LayoutParams( LinearLayout.LayoutParams.FILL_PARENT, 40 ) );
			((LinearLayout)convertView).addView( tv );
		}
		
		LinearLayout ll = (LinearLayout)convertView;
		TextView v = (TextView)ll.getChildAt( 0 );
		v.setVisibility( View.VISIBLE );
		v.setText( m_tagNames.get( position ) );
		return ll;
	}

	private fm.last.ws.EventHandler m_topTagsEventHandler = 
		new fm.last.ws.EventHandler()
	{

		public void onError( int id, String error )
		{
			// TODO Auto-generated method stub
			
		}

		public void onMethodComplete( int id, Response response )
		{
			Element topTags = (Element)response.xmlDocument().getElementsByTagName( "toptags" ).item(0);
			NodeList tags = topTags.getElementsByTagName( "tag" );
			for( int i = 0; i < tags.getLength(); i++ )
			{
				EasyElement tag = new EasyElement( (Element)tags.item( i ) );
				m_tagNames.add( tag.e("name").value() );
			}
			m_activity.runOnUiThread( new Runnable() 
			{
				public void run()
				{
					notifyDataSetChanged();
				}
				
			});
		}
		
	};
	
	private fm.last.ws.EventHandler m_similarTagsEventHandler = 
		new fm.last.ws.EventHandler()
	{

		public void onError( int id, String error )
		{
			// TODO Auto-generated method stub
			
		}

		public void onMethodComplete( int id, Response response )
		{
			Element topTags = (Element)response.xmlDocument().getElementsByTagName( "similartags" ).item(0);
			NodeList tags = topTags.getElementsByTagName( "tag" );
			for( int i = 0; i < tags.getLength(); i++ )
			{
				EasyElement tag = new EasyElement( (Element)tags.item( i ) );
				m_tagNames.add( tag.e("name").value() );
			}
			m_activity.runOnUiThread( new Runnable() 
			{
				public void run()
				{
					notifyDataSetChanged();
				}
				
			});
		}
		
	};
}
