package Interfaces;

import com.google.firebase.database.DataSnapshot;

public interface OnGetDataListener {
    void onSuccess(Iterable<DataSnapshot> dataSnapshotValue);
    void onSingleSuccess(DataSnapshot dataSnapshot);
}
