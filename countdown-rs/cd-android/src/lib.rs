/*
Copyright 2025 sby1ce

SPDX-License-Identifier: AGPL-3.0-or-later
*/

uniffi::setup_scaffolding!();

#[uniffi::export]
pub fn update_timers(now: i64, origins: Vec<i64>) -> Vec<Vec<String>> {
    cd_core::update_timers_(now, origins)
        .into_iter()
        .map(Vec::from)
        .collect()
}
