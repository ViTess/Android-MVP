package vite.androidTest;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import vite.mvp.ui.main.MainActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

/**
 * Created by trs on 17-6-5.
 */
@RunWith(AndroidJUnit4.class)
public class PageStateTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void test() {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.getActivity().showContent();
            }
        });
        sleep(3000);

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.getActivity().showEmpty();
            }
        });
        sleep(3000);

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.getActivity().showError();
            }
        });
        sleep(3000);

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.getActivity().showNetError();
            }
        });
        sleep(3000);

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.getActivity().showLoading();
            }
        });

        while (true) {
        }
    }

    public void runTestOnUiThread(final Runnable r) {
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                try {
                    r.run();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }

    public static void sleep(long m) {
        try {
            Thread.currentThread().sleep(m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
