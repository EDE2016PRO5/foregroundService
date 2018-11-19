package dk.e5pro5.foregroundservice

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView


class ListAdapter(context: Context, resource: Int, objects: MutableList<DateStepsModel>) :
    ArrayAdapter<DateStepsModel>(context, resource, objects){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        //Get the view passed in the method parameter
        var listItemView = convertView
        //If view is null inflate it with the custom_list_item
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.custom_list_item, parent, false)
        }
        val mStepCountList = getItem(position)
        val textName = listItemView?.findViewById<TextView>(R.id.mDateStepCountText)

        textName?.setText(
            (mStepCountList?.mDate.toString() + """ - Total Steps:
                    |""" + mStepCountList?.mStepCount).trimMargin())

        return listItemView as View
    }
}

/*
class ListAdapter : BaseAdapter() {
    TextView mDateStepCountText;
    ArrayList<DateStepsModel> mStepCountList;
    Context mContext;
    LayoutInflater mLayoutInflater;
    public ListAdapter(ArrayList<DateStepsModel>
            mStepCountList, Context mContext) {
        this.mStepCountList = mStepCountList;
        this.mContext = mContext;
        this.mLayoutInflater =
                (LayoutInflater)this.mContext.getSystemService
        (Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mStepCountList.size();
    }
    @Override
    public Object getItem(int position) {
        return mStepCountList.get(position);
    }
    @Override
    public long getItemId(int position) {return position;
    }
    @Override
    public View getView(int position, View convertView,
        ViewGroup parent) {
        if(convertView==null){
            convertView = mLayoutInflater.inflate
            (R.layout.list_rows, parent, false);
        }
        mDateStepCountText =
                (TextView)convertView.findViewById
                (R.id.sensor_name);
        mDateStepCountText.setText
        (mStepCountList.get(position).mDate + " - Total
                Steps: " + String.valueOf(mStepCountList.get
        (position).mStepCount));
        return convertView;
    }*/