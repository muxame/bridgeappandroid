package net.bridgeint.app.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.bridgeint.app.R;
import net.bridgeint.app.activities.BaseActivity;
import net.bridgeint.app.activities.ImagesActivity;
import net.bridgeint.app.activities.YoutubeVideoActivity;
import net.bridgeint.app.models.BannerModelForAdapter;
import net.bridgeint.app.models.UniversityModel;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.SessionManager;
import net.bridgeint.app.utils.SharedClass;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static net.bridgeint.app.utils.Constants.EVENT_UNIVERSITY_MEDIA_SCREEN;

public class MediaDetailsFragment extends BaseFragment {
    View view;
    @BindView(R.id.ic_backbtn)
    ImageView icBackbtn;
    @BindView(R.id.ivUni)
    ImageView ivUni;
    @BindView(R.id.tvUniName)
    TextView tvUniName;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvDescription)
    TextView tvDescription;
    @BindView(R.id.line)
    View line;

    Unbinder unbinder;
    @BindView(R.id.iv_image_one)
    CircleImageView ivImageOne;
    @BindView(R.id.iv_image_two)
    CircleImageView ivImageTwo;
    @BindView(R.id.iv_image_three)
    CircleImageView ivImageThree;
    public UniversityModel universityModel = null;
    @BindView(R.id.youtube_view)
    RelativeLayout youtubeView;
    @BindView(R.id.ll_image_one)
    LinearLayout llImageOne;
    @BindView(R.id.ll_image_two)
    LinearLayout llImageTwo;
    @BindView(R.id.ll_image_three)
    LinearLayout llImageThree;
    ArrayList<BannerModelForAdapter> banners2 = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_media_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ar")) {
            icBackbtn.setRotation(180);
        }
        icBackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        ((BaseActivity) getActivity()).logFirebaseEvent(EVENT_UNIVERSITY_MEDIA_SCREEN.replace(" ", "_").toLowerCase());
        if (getArguments() != null) {
//            universityModel = (UniversityModel) getIntent().getSerializableExtra(Constants.UNIVERSITY);
            universityModel = (UniversityModel) getArguments().getSerializable(Constants.UNIVERSITY);
            SharedClass.universityModel = universityModel;
            tvUniName.setText(universityModel.getTitle());
            tvAddress.setText(universityModel.getAddress());
            tvDescription.setText(universityModel.getInformation());
            if (!universityModel.getIcon().equalsIgnoreCase("")) {
                Picasso.with(getActivity()).load(Constants.IMAGE_URL + universityModel.getIcon()).into(ivUni);
            }
            if (!universityModel.getImage().equalsIgnoreCase("")) {
                llImageOne.setVisibility(View.VISIBLE);
                Picasso.with(getActivity()).load(Constants.IMAGE_URL + universityModel.getImage()).into(ivImageOne);
                banners2.add(new BannerModelForAdapter(universityModel.getImage(), false));
            } else {
                llImageOne.setVisibility(View.GONE);
            }
            if (!universityModel.getImage2().equalsIgnoreCase("")) {
                llImageTwo.setVisibility(View.VISIBLE);
                Picasso.with(getActivity()).load(Constants.IMAGE_URL + universityModel.getImage2()).into(ivImageTwo);
                banners2.add(new BannerModelForAdapter(universityModel.getImage2(), false));
            } else {
                llImageTwo.setVisibility(View.GONE);
            }
            if (!universityModel.getImage3().equalsIgnoreCase("")) {
                llImageThree.setVisibility(View.VISIBLE);
                Picasso.with(getActivity()).load(Constants.IMAGE_URL + universityModel.getImage3()).into(ivImageThree);
                banners2.add(new BannerModelForAdapter(universityModel.getImage3(), false));
            } else {
                llImageThree.setVisibility(View.GONE);
            }

            if (!universityModel.getYoutubeVideo().equalsIgnoreCase("")) {
                youtubeView.setVisibility(View.VISIBLE);
                banners2.add(new BannerModelForAdapter(universityModel.getYoutubeVideo(), true));
            } else {
                youtubeView.setVisibility(View.GONE);
            }
            llImageOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ImagesActivity.class);
                    intent.putExtra("position", 0);
                    intent.putParcelableArrayListExtra("banners", banners2);
                    startActivity(intent);
                }
            });
            llImageTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ImagesActivity.class);
                    intent.putExtra("position", 1);
                    intent.putParcelableArrayListExtra("banners", banners2);
                    startActivity(intent);
                }
            });
            llImageThree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ImagesActivity.class);
                    intent.putExtra("position", 2);
                    intent.putParcelableArrayListExtra("banners", banners2);
                    startActivity(intent);
                }
            });

            youtubeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getActivity().startActivity(new Intent(getActivity(), YoutubeVideoActivity.class).putExtra("video", universityModel.getYoutubeVideo()));
                }
            });
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }
}
