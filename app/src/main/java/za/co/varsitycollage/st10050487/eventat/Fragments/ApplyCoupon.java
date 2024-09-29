// ApplyCoupon.java
package za.co.varsitycollage.st10050487.eventat.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import java.util.Random;

import za.co.varsitycollage.st10050487.eventat.R;

public class ApplyCoupon extends Fragment {

    private BlockInfoViewModel blockInfoViewModel;

    public ApplyCoupon() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_apply_coupon, container, false);

        blockInfoViewModel = new ViewModelProvider(requireActivity()).get(BlockInfoViewModel.class);

             Button applyCouponButton = view.findViewById(R.id.TaptoApply);
        applyCouponButton.setOnClickListener(v -> applyCoupon());

        return view;
    }

    private void applyCoupon() {
        Random random = new Random();
        int discount = 10 + random.nextInt(91);

        String blockInfo = blockInfoViewModel.getBlockInfo().getValue();
        if (blockInfo != null) {
            String priceStr = blockInfo.replaceAll("[^0-9.]", "");
            int currentPrice = Integer.parseInt(priceStr);

            int newPrice = currentPrice - discount;
            String newBlockInfo = blockInfo.replace(priceStr, String.valueOf(newPrice));
            blockInfoViewModel.setBlockInfo(newBlockInfo);

            Toast.makeText(getActivity(), "Coupon applied successfully! Discount: R" + discount, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "No block info available to apply coupon.", Toast.LENGTH_LONG).show();
        }
    }
}