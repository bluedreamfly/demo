package com.amaptest.zhang.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import com.facebook.datasource.DataSource;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amaptest.zhang.R;
import com.facebook.common.references.CloseableReference;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeHolder;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.views.view.ReactViewGroup;

/**
 * Created by zhenhuihuang on 2017/7/15.
 */

public class QBMapMarkerView extends ReactViewGroup {


    private MarkerOptions markerOptions;
    private Marker marker;
    private LatLng position;
    private String title;
    private Boolean draggable;
    private float zIndex;
    private float markerHue = 0.0f;
    private BitmapDescriptor iconBitmapDescriptor;
    private Bitmap iconBitmap;

    private final DraweeHolder<?> logoHolder;
    private DataSource<CloseableReference<CloseableImage>> dataSource;
    private final ControllerListener<ImageInfo> mLogoControllerListener =
            new BaseControllerListener<ImageInfo>() {
                @Override
                public void onFinalImageSet(
                        String id,
                        @Nullable final ImageInfo imageInfo,
                        @Nullable Animatable animatable) {
                    CloseableReference<CloseableImage> imageReference = null;
                    try {
                        imageReference = dataSource.getResult();
                        if (imageReference != null) {
                            CloseableImage image = imageReference.get();
                            if (image != null && image instanceof CloseableStaticBitmap) {
                                CloseableStaticBitmap closeableStaticBitmap = (CloseableStaticBitmap) image;
                                Bitmap bitmap = closeableStaticBitmap.getUnderlyingBitmap();
                                if (bitmap != null) {
                                    bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                                    iconBitmap = bitmap;
                                    iconBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
                                }
                            }
                        }
                    } finally {
                        dataSource.close();
                        if (imageReference != null) {
                            CloseableReference.closeSafely(imageReference);
                        }
                        if (marker != null) {
                            marker.setIcon(getIcon());
                        }
                    }
                }
            };


    public QBMapMarkerView(Context context) {

        super(context);
        logoHolder = DraweeHolder.create(createDraweeHierarchy(), context);
        logoHolder.onAttach();
    }

    private GenericDraweeHierarchy createDraweeHierarchy() {
        return new GenericDraweeHierarchyBuilder(getResources())
                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                .setFadeDuration(0)
                .build();
    }
    //设置marker坐标
    public void setCoordinate(ReadableMap coordinate) {
        position = new LatLng(coordinate.getDouble("lat"), coordinate.getDouble("lng"));
        if (marker != null) {
            marker.setPosition(position);
        }
    }

    //设置marker标题
    public void setTitle(String title) {
        this.title = title;
        if (marker != null) {
            marker.setTitle(title);
        }
    }

    public void setDraggable(Boolean draggable) {
        this.draggable = draggable;
        if (marker != null) {
            marker.setDraggable(draggable);
        }
    }

    private MarkerOptions createMarkerOptions() {
        MarkerOptions options = new MarkerOptions().position(position);
        options
                .draggable(draggable)
                .title(title)
                .icon(getIcon());
//                .zIndex(zIndex)
//                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
//                        .decodeResource(getResources(), R.mipmap.ic_bike)));
        return options;
    }

    public MarkerOptions getMarkerOptions() {
        if (markerOptions == null) {
            markerOptions = createMarkerOptions();
        }
        return markerOptions;
    }
    public void addToMap(AMap map) {
        marker = map.addMarker(getMarkerOptions());
    }

    public void removeFromMap(AMap map) {
        marker.remove();
        marker = null;
    }

    public Marker getFeature() {
        return marker;
    }


    public void setImage(String uri) {
        if (uri == null) {
            iconBitmapDescriptor = null;

        } else if (uri.startsWith("http://") || uri.startsWith("https://") ||
                uri.startsWith("file://")) {
            ImageRequest imageRequest = ImageRequestBuilder
                    .newBuilderWithSource(Uri.parse(uri))
                    .build();

            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            dataSource = imagePipeline.fetchDecodedImage(imageRequest, this);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(imageRequest)
                    .setControllerListener(mLogoControllerListener)
                    .setOldController(logoHolder.getController())
                    .build();
            logoHolder.setController(controller);
            if (marker != null) {
                marker.setIcon(getIcon());
            }
        } else {
            iconBitmapDescriptor = getBitmapDescriptorByName(uri);
            if (marker != null) {
                marker.setIcon(getIcon());
            }

        }
    }

    private BitmapDescriptor getIcon() {
        if (iconBitmapDescriptor != null) {
            return iconBitmapDescriptor;
        }
//        else {
//            return BitmapDescriptorFactory.defaultMarker(this.markerHue);
//        }
        return null;

    }

    private int getDrawableResourceByName(String name) {
        return getResources().getIdentifier(
                name,
                "drawable",
                getContext().getPackageName());
    }

    private BitmapDescriptor getBitmapDescriptorByName(String name) {
        return BitmapDescriptorFactory.fromResource(getDrawableResourceByName(name));
    }

}
