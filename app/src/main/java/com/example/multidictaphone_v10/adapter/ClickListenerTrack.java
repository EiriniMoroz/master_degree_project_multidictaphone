package com.example.multidictaphone_v10.adapter;

public interface ClickListenerTrack {
    void deleteClicked(int position);
    void recordBtnClicked(int position);
    void stopRecordBtnClicked(int position);
    void playTrackBtnClicked(int position);
    void stopPlayingTrackBtnClicked(int position);

}
