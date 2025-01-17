// Copyright 2021-present StarRocks, Inc. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.starrocks.persist;

import com.google.gson.annotations.SerializedName;
import com.starrocks.common.io.Text;
import com.starrocks.common.io.Writable;
import com.starrocks.lake.snapshot.ClusterSnapshot;
import com.starrocks.lake.snapshot.ClusterSnapshotJob;
import com.starrocks.persist.gson.GsonUtils;

import java.io.DataInput;
import java.io.IOException;

public class ClusterSnapshotLog implements Writable {
    public enum ClusterSnapshotLogType { NONE, CREATE_SNAPSHOT_PREFIX, DROP_SNAPSHOT, CREATE_SNAPSHOT, UPDATE_SNAPSHOT_JOB }
    @SerializedName(value = "type")
    private ClusterSnapshotLogType type = ClusterSnapshotLogType.NONE;
    // For CREATE_SNAPSHOT_PREFIX
    @SerializedName(value = "createSnapshotNamePrefix")
    private String createSnapshotNamePrefix = "";
    @SerializedName(value = "storageVolumeName")
    private String storageVolumeName = "";
    // For DROP_SNAPSHOT
    @SerializedName(value = "dropSnapshotName")
    private String dropSnapshotName = "";
    // For CREATE_SNAPSHOT
    @SerializedName(value = "snapshot")
    private ClusterSnapshot snapshot = null;
    // For UPDATE_SNAPSHOT_JOB
    @SerializedName(value = "snapshotJob")
    private ClusterSnapshotJob snapshotJob = null;

    public ClusterSnapshotLog() {}

    public void setCreateSnapshotNamePrefix(String createSnapshotNamePrefix, String storageVolumeName) {
        this.type = ClusterSnapshotLogType.CREATE_SNAPSHOT_PREFIX;
        this.createSnapshotNamePrefix = createSnapshotNamePrefix;
        this.storageVolumeName = storageVolumeName;
    }

    public void setDropSnapshot(String dropSnapshotName) {
        this.type = ClusterSnapshotLogType.DROP_SNAPSHOT;
        this.dropSnapshotName = dropSnapshotName;
    }

    public void setCreateSnapshot(ClusterSnapshot snapshot) {
        this.type = ClusterSnapshotLogType.CREATE_SNAPSHOT;
        this.snapshot = snapshot;
    }

    public void setSnapshotJob(ClusterSnapshotJob job) {
        this.type = ClusterSnapshotLogType.UPDATE_SNAPSHOT_JOB;
        this.snapshotJob = job;
    }

    public ClusterSnapshotLogType getType() {
        return type;
    }

    public String getCreateSnapshotNamePrefix() {
        return this.createSnapshotNamePrefix;
    }

    public String getStorageVolumeName() {
        return this.storageVolumeName;
    }

    public String getDropSnapshotName() {
        return this.dropSnapshotName;
    }

    public ClusterSnapshot getSnapshot() {
        return this.snapshot;
    }

    public ClusterSnapshotJob getSnapshotJob() {
        return this.snapshotJob;
    }

    public static ClusterSnapshotLog read(DataInput in) throws IOException {
        String json = Text.readString(in);
        return GsonUtils.GSON.fromJson(json, ClusterSnapshotLog.class);
    }


}