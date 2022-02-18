package com.mooc.ppjoke;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

public abstract class AbsViewModel<T> extends ViewModel {

    private static final String TAG = "dataSource";
    private DataSource dataSource;
    private final LiveData<PagedList<T>> pageData;

    private MutableLiveData<Boolean> boundaryPageData = new MutableLiveData<>();
    protected PagedList.Config config;

    public AbsViewModel() {

        config = new PagedList.Config.Builder()
                .setPageSize(10)
                .setInitialLoadSizeHint(12)
//                .setMaxSize(100)
                .build();

        pageData = new LivePagedListBuilder(factory, config)
                .setInitialLoadKey(0)
//                .setFetchExecutor()
                .setBoundaryCallback(callback)
                .build();

        pageData.observeForever(new Observer<PagedList<T>>() {
            @Override
            public void onChanged(PagedList<T> pagedList) {
//                adapter.submitList();
            }
        });
    }

    public LiveData<PagedList<T>> getPageData() {
        return pageData;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public MutableLiveData<Boolean> getBoundaryPageData() {
        return boundaryPageData;
    }

    PagedList.BoundaryCallback<T> callback = new PagedList.BoundaryCallback<T>() {
        @Override
        public void onZeroItemsLoaded() {
            boundaryPageData.postValue(false);
        }

        @Override
        public void onItemAtFrontLoaded(@NonNull T itemAtFront) {
            boundaryPageData.postValue(true);
        }

        @Override
        public void onItemAtEndLoaded(@NonNull T itemAtEnd) {
            super.onItemAtEndLoaded(itemAtEnd);
        }
    };

    DataSource.Factory factory = new DataSource.Factory() {
        @NonNull
        @Override
        public DataSource create() {
            if (dataSource == null || dataSource.isInvalid()) {
                dataSource = createDataSource();
                Log.d(TAG, "=========================dataSource is empty============================");
            }
//            Log.d(TAG, "===================dataSource is empty not null=======================");
            return dataSource;
        }
    };

    public abstract DataSource createDataSource();
}
