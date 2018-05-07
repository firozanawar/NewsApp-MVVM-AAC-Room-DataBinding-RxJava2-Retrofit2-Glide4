package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.R;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.databinding.ActivityNewsBinding;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.helper.Constants;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.news.NewsFragment;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.settings.SettingsActivity;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.util.ConnectionSniffer;

import java.util.Objects;

import static com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.repository.INewsAPI.Endpoints.EVERYTHING;
import static com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.repository.INewsAPI.Endpoints.SOURCES;
import static com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.repository.INewsAPI.Endpoints.TOP_HEADLINES;

public class NewsActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ActivityNewsBinding mActivityNewsBinding;
    private MenuItem mPrevMenuItem;
    private long mBackPressed;
    private boolean mIsActiveConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityNewsBinding = DataBindingUtil.setContentView(this, R.layout.activity_news);
        setSupportActionBar(Objects.requireNonNull(mActivityNewsBinding.layoutToolbar).toolbar);

        mIsActiveConnection = ConnectionSniffer.sniff();

        if (!mIsActiveConnection) {
            createSnackBar(getString(R.string.snackBar_offline_warning_text));
        }

        setupViewPager();
    }

    private void setupViewPager() {
        mActivityNewsBinding.vpNews.setAdapter(new ViewPagerInflater(getSupportFragmentManager()));
        mActivityNewsBinding.vpNews.setOffscreenPageLimit(Constants.VIEW_PAGER_FRAGMENT_COUNT);
        mActivityNewsBinding.vpNews.setPageTransformer(true, new DepthPageTransformer());

        mActivityNewsBinding.bnvNews.setOnNavigationItemSelectedListener(this);

        mActivityNewsBinding.vpNews.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mPrevMenuItem != null) {
                    mPrevMenuItem.setChecked(false);
                } else {
                    mActivityNewsBinding.bnvNews.getMenu().getItem(0).setChecked(false);
                }
                mActivityNewsBinding.bnvNews.getMenu().getItem(position).setChecked(true);
                mPrevMenuItem = mActivityNewsBinding.bnvNews.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.search_menu);
        mActivityNewsBinding.layoutToolbar.searchView.setMenuItem(menuItem);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClicked = item.getItemId();

        mIsActiveConnection = ConnectionSniffer.sniff();

        switch (itemThatWasClicked) {
            case R.id.settings_menu:
                if (mIsActiveConnection) {
                    startActivity(new Intent(NewsActivity.this, SettingsActivity.class));
                    return true;
                } else {
                    createSnackBar(getString(R.string.snackBar_warning_text));
                    return false;
                }

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createSnackBar(String content) {
        final Snackbar snack = Snackbar.make(mActivityNewsBinding.vpNews,
                content, Snackbar.LENGTH_LONG);

        View view = snack.getView();

        TextView snackText = view.findViewById(android.support.design.R.id.snackbar_text);
        snackText.setTextColor(ContextCompat.getColor(this, R.color.white));

        Button snackButton = view.findViewById(android.support.design.R.id.snackbar_action);
        snackButton.setTextColor(ContextCompat.getColor(this, R.color.snackBarActionColor));

        mActivityNewsBinding.bnvNews.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mActivityNewsBinding.bnvNews.getViewTreeObserver().removeOnPreDrawListener(this);
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)
                        snack.getView().getLayoutParams();
                params.setMargins(0, 0, 0, mActivityNewsBinding.bnvNews.getHeight());
                snack.getView().setLayoutParams(params);

                return false;
            }
        });

        snack.setAction(getString(R.string.snackBar_action_text), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snack.dismiss();
            }
        });
        snack.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemThatWasClicked;

        if (item.getItemId() < 0) {
            itemThatWasClicked = 0;
        } else {
            itemThatWasClicked = item.getItemId();
        }

        switch (itemThatWasClicked) {
            case R.id.top_headlines:
                mActivityNewsBinding.vpNews.setCurrentItem(0);
                return true;

            case R.id.everything:
                mActivityNewsBinding.vpNews.setCurrentItem(1);
                return true;

            case R.id.sources:
                mActivityNewsBinding.vpNews.setCurrentItem(2);
                return true;

            default:
                break;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        int timeDelay = getResources().getInteger(R.integer.delay_in_seconds_to_close);

        if (mBackPressed + timeDelay > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(getBaseContext(), getString(R.string.twice_press_to_exit),
                    Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private class ViewPagerInflater extends FragmentStatePagerAdapter {
        ViewPagerInflater(FragmentManager fm) {
            super(fm);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.top_headlines_header);

                case 1:
                    return getString(R.string.everything_header);

                case 2:
                    return getString(R.string.sources_header);

                default:
                    break;
            }

            return null;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            if (object instanceof NewsFragment) {
                return POSITION_UNCHANGED;
            } else {
                return POSITION_NONE;
            }
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return NewsFragment.getInstance(TOP_HEADLINES);

                case 1:
                    return NewsFragment.getInstance(EVERYTHING);

                case 2:
                    return NewsFragment.getInstance(SOURCES);

                default:
                    break;
            }

            return null;
        }

        @Override
        public int getCount() {
            return Constants.VIEW_PAGER_FRAGMENT_COUNT;
        }
    }
}