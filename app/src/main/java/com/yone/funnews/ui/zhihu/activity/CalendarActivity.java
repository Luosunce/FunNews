package com.yone.funnews.ui.zhihu.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.yone.funnews.R;
import com.yone.funnews.base.SimpleActivity;
import com.yone.funnews.component.RxBus;
import com.yone.funnews.util.DateUtil;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Yoe on 2016/10/14.
 */

public class CalendarActivity extends SimpleActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolBar;
    @BindView(R.id.view_calendar)
    MaterialCalendarView mCalendar;

    CalendarDay mDate;

    @Override
    protected int getLayout() {
        return R.layout.activity_calendar;
    }

    @Override
    protected void initEventAndData() {
        setToolBar(mToolBar,"选择日期");
        mCalendar.state().edit()
                .setFirstDayOfWeek(Calendar.WEDNESDAY)
                .setMinimumDate(CalendarDay.from(2013,5,20))
                .setMaximumDate(CalendarDay.from(DateUtil.getCurrentYear(),DateUtil.getCurrentMonth(),DateUtil.getCurrentDay()))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        mCalendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                mDate = date;
            }
        });
    }


    @OnClick(R.id.tv_calendar_enter)
    void chooseDate() {
        RxBus.getDefault().post(mDate);
        finish();
    }

}
